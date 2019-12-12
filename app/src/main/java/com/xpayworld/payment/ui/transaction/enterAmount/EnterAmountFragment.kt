package com.xpayworld.payment.ui.transaction.enterAmount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.xpayworld.payment.R
import com.xpayworld.payment.databinding.FragmentEnterAmountBinding
import com.xpayworld.payment.network.transaction.PaymentType
import com.xpayworld.payment.network.transaction.TransactionPurchase
import com.xpayworld.payment.ui.base.kt.BaseFragment
import com.xpayworld.payment.ui.dashboard.DrawerLocker
import com.xpayworld.payment.ui.transaction.processTransaction.ARG_AMOUNT
import com.xpayworld.payment.util.InjectorUtil
import com.xpayworld.payment.util.formattedAmount
import com.xpayworld.payment.util.paymentType
import kotlinx.android.synthetic.main.fragment_enter_amount.*
import kotlinx.android.synthetic.main.view_number_pad.*


class EnterAmountFragment : BaseFragment() {

    var btnNumpad = listOf<Button>()
    var btnTransType = listOf<Button>()
    var viewTransType = listOf(R.drawable.tab_indenticator, R.drawable.tab_indenticator_clear)
    var amountStr = ""

    private  val viewModel : AmountViewModel by viewModels {
        InjectorUtil.provideAmountViewModelFactory(requireContext())
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEnterAmountBinding.inflate(inflater, container, false)
        binding.vwNumpad.viewModel = viewModel
        binding.lifecycleOwner = this@EnterAmountFragment
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            amountStr = it.getString(ARG_AMOUNT).toString()
        }
        paymentType  = PaymentType.CREDIT(TransactionPurchase.Action.EMV)
    }


    override fun initView(view: View,container: ViewGroup?) {
        // Numpad Button
        // Numpad Button Image
        btnNumpad = listOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0)
        btnTransType = listOf(btnCredit,btnDebit)

        // Numpad Click Listener
        btnNumpad.forEach { it.setOnClickListener { v ->
            val len = amountStr.length
            if (len == 8) return@setOnClickListener
            if (len == 0 && (v as Button).text == "0")  return@setOnClickListener
            amountStr += (v as Button).text
            viewModel.displayAmount.value = formattedAmount(amountStr)
            viewModel.amountStr.value = amountStr
          }
        }

        // Update indenticator on  transaction typ
        btnTransType.forEach { it.setOnClickListener { v->
                val btn = v as Button
                if (btn.text == "Credit") {
                    paymentType  = PaymentType.CREDIT(TransactionPurchase.Action.EMV)
                    btnCredit.setBackgroundResource(viewTransType[0])

                } else {
                    paymentType  = PaymentType.DEBIT(PaymentType.DebitTransaction.SALE, TransactionPurchase.AccountType.SAVINGS)
                    btnDebit.setBackgroundResource(viewTransType[1])
                }

        } }


        btnClear.setOnClickListener {
            amountStr = amountStr.dropLast(1)
            viewModel.displayAmount.value = formattedAmount(amountStr)
        }

        btnOk.setOnClickListener(viewModel.okClickListener)

//        var paymentType: PaymentType = PaymentType.DEBIT(PaymentType.DebitTransaction.SALE, TransactionPurchase.AccountType.SAVINGS)
//        when (paymentType) {
//            is PaymentType.DEBIT -> {
//                paymentType.debit.stringValue
//                paymentType.accountType
//            }
//        }

        viewModel.deviceError.observe(this , Observer { msg ->
            showError(msg.first,msg.second)
        })

    }



    override fun onResume() {
        super.onResume()

        (activity as DrawerLocker).drawerEnabled(true)
    }

}

