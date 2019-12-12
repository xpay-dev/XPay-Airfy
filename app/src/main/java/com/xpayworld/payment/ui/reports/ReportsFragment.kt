package com.xpayworld.payment.ui.reports


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xpayworld.payment.R
import com.xpayworld.payment.databinding.FragmentPreferenceBinding
import com.xpayworld.payment.databinding.FragmentReportsBinding
import com.xpayworld.payment.ui.base.kt.BaseFragment


class  ReportsFragment : BaseFragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView(view: View, container: ViewGroup?) {

    }

}
