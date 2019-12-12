package com.xpayworld.sdk

import android.content.Context
import android.content.Intent

interface  ITransCall{
    fun callTransaction(context: Context, params : XpayRequest): Intent
    fun callHistory()
    fun callPreference()
    fun callEnterPin()
    fun callActivation()
}