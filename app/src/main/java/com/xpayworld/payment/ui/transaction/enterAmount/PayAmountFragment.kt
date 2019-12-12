package com.xpayworld.payment.ui.transaction.enterAmount

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.xpayworld.payment.R
import com.xpayworld.payment.databinding.FragmentEnterAmountBinding
import com.xpayworld.payment.databinding.FragmentPayAmountBinding
import com.xpayworld.payment.network.transaction.PaymentType
import com.xpayworld.payment.network.transaction.TransactionPurchase
import com.xpayworld.payment.ui.base.kt.BaseFragment
import com.xpayworld.payment.ui.dashboard.DrawerLocker
import com.xpayworld.payment.ui.transaction.processTransaction.ARG_AMOUNT
import com.xpayworld.payment.ui.transaction.receipt.ReceiptFragmentDirections
import com.xpayworld.payment.util.InjectorUtil
import com.xpayworld.payment.util.formattedAmount
import com.xpayworld.payment.util.paymentType
import kotlinx.android.synthetic.main.fragment_pay_amount.*

class PayAmountFragment : BaseFragment(){

    var amountStr = ""
    private  val viewModel : AmountViewModel by viewModels {
        InjectorUtil.provideAmountViewModelFactory(requireContext())
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPayAmountBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this@PayAmountFragment
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            amountStr = it.getString(ARG_AMOUNT).toString()
            viewModel.amountStr.value = amountStr
            viewModel.displayAmount.value = formattedAmount(amountStr)
        }
        paymentType  = PaymentType.CREDIT(TransactionPurchase.Action.EMV)
    }
    override fun initView(view: View, container: ViewGroup?) {
        setHasOptionsMenu(true)
        btnPay.setOnClickListener(viewModel.okClickListener)
        viewModel.deviceError.observe(this , Observer { msg ->
            showError(msg.first,msg.second)
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater = activity?.menuInflater
        inflater?.inflate(R.menu.menu_pay, menu)
        super.onCreateOptionsMenu(menu, inflater!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionOffline -> {
                val direction = PayAmountFragmentDirections.actionPayAmountFragmentToOfflineTransactionFragment()
                view?.findNavController()?.navigate(direction)
            }
            R.id.actionDevice ->{
                val direction = PayAmountFragmentDirections.actionPayAmountFragmentToPreferenceFragment()
                view?.findNavController()?.navigate(direction)
            }
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}