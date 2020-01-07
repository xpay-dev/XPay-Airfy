package com.xpayworld.payment.ui.transaction.enterAmount

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.xpayworld.payment.R
import com.xpayworld.payment.databinding.FragmentEnterAmountBinding
import com.xpayworld.payment.databinding.FragmentPayAmountBinding
import com.xpayworld.payment.network.transaction.PaymentType
import com.xpayworld.payment.network.transaction.TransactionPurchase
import com.xpayworld.payment.ui.base.kt.BaseFragment
import com.xpayworld.payment.ui.dashboard.DrawerLocker
import com.xpayworld.payment.ui.transaction.processTransaction.ARG_AMOUNT
import com.xpayworld.payment.ui.transaction.processTransaction.ARG_CURRENCY
import com.xpayworld.payment.util.InjectorUtil
import com.xpayworld.payment.util.formattedAmount
import com.xpayworld.payment.util.paymentType
import kotlinx.android.synthetic.main.fragment_pay_amount.*

class PayAmountFragment : BaseFragment(){

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
        arguments?.takeIf {it.containsKey(ARG_AMOUNT).apply {

            val  amountStr = it.getString(ARG_AMOUNT).toString()
            val  currencyStr = it.get(ARG_CURRENCY).toString()

            amountStr ?: "0"
            currencyStr?: "PHP"

            viewModel.amountStr.value =  amountStr
            viewModel.btnPayEnabled.value = true
            viewModel.displayCurrency.value = currencyStr
            viewModel.displayAmount.value = formattedAmount(amountStr)
            }
        }

    }
    override fun initView(view: View, container: ViewGroup?) {
        setHasOptionsMenu(true)


        paymentType  = PaymentType.CREDIT(TransactionPurchase.Action.EMV)
        btnPay.setOnClickListener(viewModel.okClickListener)

        viewModel.deviceError.observe(this , Observer { msg ->
            showError(msg.first,msg.second)
        })

        viewModel.navigateToEnterPin.observe(this , Observer {
            val directions = PayAmountFragmentDirections.navigateToEnterPinFragment()
             if (it) findNavController().navigate(directions)
        })

        viewModel.navigateToActivation.observe(this, Observer {
            val directions = PayAmountFragmentDirections.navigateToActivationFragment()
            if (it) findNavController().navigate(directions)
        })

        viewModel.btnPayEnabled.observe( this , Observer {
            btnPay.isEnabled = it
        })

        viewModel.loadingVisibility.observe(this, Observer{
            isShow -> if (isShow == true) showProgress() else hideProgress()
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater = activity?.menuInflater
        inflater?.inflate(R.menu.menu_pay, menu)
        super.onCreateOptionsMenu(menu, inflater!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionDevice ->{
                val direction = PayAmountFragmentDirections.navigateToPreferenceFragment()
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