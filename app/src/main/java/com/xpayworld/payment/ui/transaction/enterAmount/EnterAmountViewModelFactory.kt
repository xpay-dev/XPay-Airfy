package com.xpayworld.payment.ui.transaction.enterAmount

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class  EnterAmountViewModelFactory( private  val context: Context) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AmountViewModel(context) as T
    }
}