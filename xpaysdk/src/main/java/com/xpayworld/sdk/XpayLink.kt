package com.xpayworld.sdk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder

class  XpayLink : ITransCall{
    override fun callHistory() {

    }

    override fun callPreference() {

    }

    override fun callEnterPin() {
    }

    override fun callActivation() {
    }

    //
    override fun callTransaction(context: Context, params: XpayRequest): Intent {

        val  i = context.packageManager.getLaunchIntentForPackage(XPAY_LINK)
        val gson = GsonBuilder().setPrettyPrinting().create()
        val gsonStr = gson.toJson(params)
        i!!.putExtra(XPAY_REQUEST,gsonStr)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return  i
    }

    init {
        INSTANCE = this
    }

    companion object{
        var INSTANCE : XpayLink
        init {
            INSTANCE = XpayLink()
        }
    }
}