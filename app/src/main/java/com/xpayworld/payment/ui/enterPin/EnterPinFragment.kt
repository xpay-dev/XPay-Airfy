package com.xpayworld.payment.ui.enterPin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.xpayworld.payment.R
import com.xpayworld.payment.ui.base.kt.BaseFragment
import com.xpayworld.payment.ui.dashboard.ToolbarDelegate
import com.xpayworld.payment.util.SharedPrefStorage

import androidx.lifecycle.Observer
import com.xpayworld.payment.data.AppDatabase
import com.xpayworld.payment.data.Transaction
import com.xpayworld.payment.databinding.FragmentEnterPinBinding
import com.xpayworld.payment.util.ACTIVATION_KEY
import com.xpayworld.payment.util.InjectorUtil
import kotlinx.android.synthetic.main.fragment_activation_code.*
import kotlinx.android.synthetic.main.fragment_enter_pin.*

import kotlinx.android.synthetic.main.view_number_pad.*
import kotlinx.android.synthetic.main.view_number_pad.btnClear
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class EnterPinFragment : BaseFragment() {


    var numpad = listOf<Button>()
    var pinCodeImgArr = listOf<ImageView>()

    private val viewModel: EnterPinViewModel by viewModels {
        InjectorUtil.provideEnterPinViewModelFactory(requireContext())
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEnterPinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView(view: View, container: ViewGroup?) {


        // Input
        pinCodeImgArr = listOf(img1, img2, img3, img4)
        numpad = listOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0)
        numpad.forEach { it.setOnClickListener(viewModel.numpadClickListener) }

        btnClear.setOnClickListener(viewModel.clearClickListener)
        btnOk.setOnClickListener {
            viewModel.callEnterPinAPI()
        }

        shouldCheckActivationKey()
          //InjectorUtil.getTransactionRepository(requireContext()).deleteAllTransaction()
        viewModel.pinCode
                .observe(this, Observer {
                    shouldUpdateCodeImage(it)
                })
        viewModel.toolbarVisibility.observe(this, Observer { (activity as ToolbarDelegate).showToolbar(it) })
        viewModel.loadingVisibility.observe(this, Observer {
            if (it) showProgress() else hideProgress()
        })

        viewModel.requestError.observe(this, Observer { it ->
            if (it is Boolean) {
                val shake = AnimationUtils.loadAnimation(context!!, R.anim.pin_shake)
                pinCodeImgArr.forEach {
                    it.startAnimation(shake)
                }
            }
        })
        viewModel.networkError.observe(this, Observer {
            showNetworkError()
        })

        viewModel.navigateToEnterAmount.observe(this, Observer {
            val direction = EnterPinFragmentDirections.actionEnterPinFragmentToTransactionFragment(it)
            findNavController().navigate(direction)
        })

    }

    private fun shouldCheckActivationKey() {
        val sharedPref = context?.let { it -> SharedPrefStorage(it) }
        if (sharedPref!!.isEmpty(ACTIVATION_KEY)) {
            findNavController().navigate(R.id.activationFragment)
        }
    }

    private fun shouldUpdateCodeImage(pinCode: String) {
        for (x in 0 until pinCodeImgArr.size) {
            val img = if (pinCode.length >= x + 1) R.drawable.ic_pin_cirlce_dot else R.drawable.ic_pin_circle
            pinCodeImgArr[x].setBackgroundResource(img)
        }
    }

}