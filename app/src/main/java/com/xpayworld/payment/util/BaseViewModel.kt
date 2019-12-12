package com.xpayworld.payment.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel(){

    val loadingVisibility : MutableLiveData<Boolean> = MutableLiveData()
    val networkError : MutableLiveData<String> = MutableLiveData()
    val requestError :  MutableLiveData<Any> = MutableLiveData()
    val toolbarVisibility: MutableLiveData<Boolean> = MutableLiveData()
}