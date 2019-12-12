package com.xpayworld.payment.ui.logout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.xpayworld.payment.R
import com.xpayworld.payment.databinding.FragmentLogoutBinding
import com.xpayworld.payment.databinding.FragmentReportsBinding
import com.xpayworld.payment.ui.base.kt.BaseFragment

class LogoutFragment  : BaseFragment(){

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLogoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView(view: View, container: ViewGroup?) {
        view.findNavController().navigate(R.id.enterPinFragment)
    }

}