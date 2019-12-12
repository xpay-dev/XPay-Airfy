package com.xpayworld.payment.ui.link

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xpayworld.payment.ui.enterPin.EnterPinViewModel

class LinkViewModelFactory(private val context: Context): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LinkViewModel(context) as T
    }}