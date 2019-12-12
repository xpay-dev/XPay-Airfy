package com.xpayworld.payment.ui.transaction.signature

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.gson.GsonBuilder
import com.xpayworld.payment.databinding.FragmentSignatureBinding
import com.xpayworld.payment.ui.base.kt.BaseFragment
import com.xpayworld.payment.ui.transaction.processTransaction.ARG_AMOUNT
import com.xpayworld.payment.util.*
import com.xpayworld.sdk.XPAY_REQUEST
import com.xpayworld.sdk.XPAY_RESPONSE
import kotlinx.android.synthetic.main.fragment_signature.*
import java.io.ByteArrayOutputStream
import com.xpayworld.payment.ui.dashboard.DashboardActivity as DashboardActivity1


class SignatureFragment : BaseFragment() {

    var amountStr = ""
    var imageStr = ""
    private val viewModel: SignatureViewModel by viewModels {
        InjectorUtil.provideSignatureViewModel(requireContext())
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSignatureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            amountStr = it.getString(ARG_AMOUNT).toString()
        }
    }


    override fun initView(view: View , container: ViewGroup?) {

        tvAmount.text = formattedAmount(amountStr)

        btnSubmit.setOnClickListener {

            if (!vwSignature.isBitmapEmpty) {
            val sign = vwSignature.signatureBitmap
            val baos = ByteArrayOutputStream()
            sign.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
                imageStr = b.toHexString()
                if (IS_TRANSACTION_OFFLINE){
                  this.actionTransactionOffline()
                } else {
                    actionTransactionOnline()
                }

            }
        }

        btnClear.setOnClickListener {
            vwSignature.clearCanvas()
            lblSignature.visibility = View.VISIBLE
        }

        (activity as DashboardActivity1).UserInteraction.observe(this , Observer {
            lblSignature.visibility = View.INVISIBLE
        })
    }

    fun actionTransactionOffline(){
        val txnDao = InjectorUtil.getTransactionRepository(requireContext())
        txnDao.updateSignatureTransaction(imageStr, transaction.orderId)
        val i = Intent()
        i.putExtra(XPAY_RESPONSE, SDK_XPAY_RESPONSE)
        activity!!.setResult(Activity.RESULT_OK,i)
        activity?.finish()
    }


    fun actionTransactionOnline(){
        viewModel.callSignatureAPI(imgStr = imageStr , imageLen = "${imageStr.length}",transNumber = transactionResponse!!.transNumber!!)
        val gson = GsonBuilder().setPrettyPrinting().create()
        val gsonStr = gson.toJson(transactionResponse)
        val respStr = gson.toJson(transactionResponse?.result)
        val direction = SignatureFragmentDirections.actionSignatureFragmentToReceiptFragment(gsonStr,respStr)
        view?.findNavController()?.navigate(direction)
    }



    fun ByteArray.toHexString() : String {
        return this.joinToString("") {
            java.lang.String.format("%02x", it)
        }
    }
}
