package com.xpayworld.payment.ui.link

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xpayworld.payment.databinding.FragmentLinkBinding
import com.xpayworld.payment.network.PosWsResponse
import com.xpayworld.payment.network.transaction.PaymentType
import com.xpayworld.payment.network.transaction.TransactionPurchase
import com.xpayworld.payment.ui.base.kt.BaseFragment
import com.xpayworld.payment.util.*
import com.xpayworld.sdk.XPAY_REQUEST
import com.xpayworld.sdk.XPAY_RESPONSE
import com.xpayworld.sdk.XpayRequest


class LinkFragment : BaseFragment() {

    var request = ""
    val gson = Gson()

    private val viewModel: LinkViewModel by viewModels {
        InjectorUtil.provideLinkViewModel(requireContext())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            request = it.getString(XPAY_REQUEST).toString()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLinkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView(view: View, container: ViewGroup?) {

        val data = gson.fromJson(request, XpayRequest::class.java)

        // initialization
        externalPackageName = data.appPackageName
        IS_TRANSACTION_OFFLINE = data.isOffine
        transaction.orderId = data.transactionId
        transaction.cardCaptureMethod = data.cardCaptureMethod
        transaction.currency = data.currency
        transaction.currencyCode = data.currencyCode

        if (data.isOffine){

            val strAmount = "${data.amountPurchase}".replace(".", "")
            val direction = LinkFragmentDirections.actionLinkFragmentToPayAmountFragment(strAmount)
            findNavController().navigate(direction)
            return
        }

        viewModel.loadingVisibility.observe(this, Observer {
            if (it) showProgress() else hideProgress()
        })

        // Network Error
        viewModel.networkError.observe(this, Observer {
            showNetworkError(title = it, callBack = { activity?.finish() })
        })

        viewModel.requestError.observe(this, Observer {
            val response = it as PosWsResponse
            showNetworkError(title = "REQUEST ERROR ${response.errNumber}", callBack = {

                val gson = GsonBuilder().setPrettyPrinting().create()
                val i = Intent()
                i.putExtra(XPAY_RESPONSE,  gson.toJson(response))
                activity?.setResult(Activity.RESULT_OK, i)
                activity?.finish()

            })
        })

        viewModel.navigateToNextEntry.observe(this, Observer {

            IS_SDK = true

            paymentType = PaymentType.CREDIT(TransactionPurchase.Action.EMV)
            transaction.amount = data.amountPurchase

            val strAmount = "${data.amountPurchase}".removePrefix(".")
            val direction = LinkFragmentDirections.actionLinkFragmentToPayAmountFragment(strAmount)
            findNavController().navigate(direction)


//            when (entryPoint) {
//                TRANSACTION -> {
//                    paymentType  = PaymentType.CREDIT(TransactionPurchase.Action.EMV)
//                    transaction.amount = data.amountPurchase
//                    val strAmount = "${data.amountPurchase}".removePrefix(".")
//                    val direction = LinkFragmentDirections.actionLinkFragmentToProcessTranactionFragment(strAmount)
//                    findNavController().navigate(direction)
//                }
//                ACTIVATION -> {
//
//                }
//                ENTER_PIN -> {
//
//                }
//                PREFERENCE -> {
//
//                }
//            }
        })
    }


}
