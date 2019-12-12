package com.xpayworld.payment.ui.history

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xpayworld.payment.ui.enterPin.EnterPinViewModel

class TransactionHistoryViewModelFactory(private val context: Context): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TransactionHistoryViewModel(context) as T
    }}