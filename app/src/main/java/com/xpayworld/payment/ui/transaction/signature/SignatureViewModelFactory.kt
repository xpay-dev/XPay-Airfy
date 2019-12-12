package com.xpayworld.payment.ui.transaction.signature

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class  SignatureViewModelFactory(private  val context: Context) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignatureViewModel(context) as T
    }
}