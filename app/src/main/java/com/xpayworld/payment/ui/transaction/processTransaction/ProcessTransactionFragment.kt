package com.xpayworld.payment.ui.transaction.processTransaction

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.gson.GsonBuilder
import com.xpayworld.payment.R
import com.xpayworld.payment.databinding.FragmentProcessTransactionBinding
import com.xpayworld.payment.network.PosWsResponse
import com.xpayworld.payment.ui.dashboard.DrawerLocker
import com.xpayworld.payment.ui.dashboard.ToolbarDelegate
import com.xpayworld.payment.util.formattedAmount
import com.xpayworld.payment.util.IS_SDK
import com.xpayworld.payment.util.IS_TRANSACTION_OFFLINE
import com.xpayworld.payment.util.transaction
import com.xpayworld.sdk.XPAY_RESPONSE
import com.xpayworld.sdk.XpayResponse
import kotlinx.android.synthetic.main.fragment_process_transaction.*



@Suppress("UNCHECKED_CAST")
class ProcessTransactionFragment : BaseDeviceFragment() {

    var viewModel: ProcessTransactionViewModel? = null


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProcessTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView(view: View, container: ViewGroup?) {
        if (amountStr.contains("."))
            amountStr = amountStr.replace(".","")

        tvAmount.text = formattedAmount(amountStr)
        transaction.amount = ( amountStr.toInt()/100.0)

        viewModel = ViewModelProviders.of(this).get(ProcessTransactionViewModel::class.java)

        startAnimation.observe(this, Observer {
            if (it) imageProcess().start() else imageProcess().stop()
        })

        toolbarTitle.observe(this, Observer((activity as ToolbarDelegate)::setTitle))

        cancelVisibility.observe(this, Observer {
            btnCancel.visibility = it})

        checkBluetoothPermission.observe(this , Observer {
            if (it) {
                showError("Bluetooth is denied", "This application requires bluetooth connection to process transaction, Please go to settings to enable bluetooth permission")
            }
        })

        //Progress dialog
        viewModel?.loadingVisibility?.observe(this, Observer { isShow ->
            if (isShow == true) showProgress() else hideProgress()
        })

        // Network Error
        viewModel?.networkError?.observe(this, Observer {
            btnCancel.visibility = View.INVISIBLE
            showNetworkError(title = it ,callBack = {
                if (IS_SDK){
                    activity?.finish()
                } else {
                    view.findNavController().popBackStack(R.id.transactionFragment, true)
                }
            })
        })

        viewModel?.requestError?.observe(this, Observer {
            if (it is PosWsResponse){
                val response = it
                btnCancel.visibility = View.INVISIBLE

                showError(title = "REQUEST ERROR ${response.errNumber}", message = response.message?: "", callBack = {
                    if (IS_SDK){
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val i = Intent()
                        i.putExtra(XPAY_RESPONSE,gson.toJson(response))
                        activity!!.setResult(Activity.RESULT_OK,i)
                        activity?.finish()
                    } else {
                        view.findNavController().popBackStack(R.id.transactionFragment, true)
                    }
              })
            } else if (it is Int){
                if (it == -2){
                    showError(title = "TRANSACTION DECLINED", message = "You're card has already expired", callBack = {
                        if (IS_SDK){
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val i = Intent()

                            var xPayResponse = XpayResponse()
                            xPayResponse.response = "-2"
                            xPayResponse.cardNumber =  ""
                            xPayResponse.maskedCard = ""
                            xPayResponse.expiry = ""
                            xPayResponse.responseMsg =  "DECLINED"
                            i.putExtra(XPAY_RESPONSE,gson.toJson(xPayResponse))
                            activity!!.setResult(Activity.RESULT_OK,i)
                            activity?.finish()
                        }
                    })
                }
            }
        })

        if (!IS_TRANSACTION_OFFLINE){
            // Calling Transaction API
            proceedTransaction.observe(this, Observer {
                viewModel?.callTransactionAPI()
            })
        } else {
            // process offline transaction
            proceedTransaction.observe(this, Observer {
                viewModel?.callOfflineTransction(requireContext())
            })
        }

        // Transaction API Result
        viewModel?.onlineAuthResult?.observe(this, Observer {
            onlineProcessResult.value = it
        })

        // Device Transaction Result
        onTransactionResult.observe(this, Observer {
            val direction = ProcessTransactionFragmentDirections.actionProcessTransactionToSignatureFragment(amountStr)
            if (it) view.findNavController().navigate(direction)
        })

        cancelTitle.observe(this, Observer {
            btnCancel.text = it
        })

        btnCancel.setOnClickListener {

            if (!IS_TRANSACTION_OFFLINE) {
                view.findNavController().popBackStack(R.id.transactionFragment, true)

            } else {
                val i = Intent()
                i.putExtra(XPAY_RESPONSE,"")
                activity!!.setResult(Activity.RESULT_OK,i)
                activity?.finish()
            }
        }
    }

    private fun imageProcess(): AnimationDrawable {
        val img = view!!.findViewById<ImageView>(R.id.imgProcess)
        return img?.drawable as AnimationDrawable
    }


    override fun onResume() {
        super.onResume()

        (activity as DrawerLocker).drawerEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}