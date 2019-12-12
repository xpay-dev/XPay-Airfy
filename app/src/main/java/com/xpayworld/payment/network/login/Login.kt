package com.xpayworld.payment.network.login

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.xpayworld.payment.network.PosWsRequest
import com.xpayworld.payment.util.ACTIVATION_KEY
import com.xpayworld.payment.util.SharedPrefStorage
import com.xpayworld.payment.util.POS_REQUEST

class Login (context : Context) {
    @SerializedName("AppVersion")
    var appVersion = ""

    @SerializedName("Pin")
    var pin = ""

    @SerializedName("Password")
    var password = "M_HPay12"

    @SerializedName("Username")
     var userName = "M_HPay"

    @SerializedName("POSWSRequest")
     var posWsRequest: PosWsRequest? = null

    init {
        val sharedPref = context.let { SharedPrefStorage(it) }
        val posReq = PosWsRequest(context)
        posReq.activationKey = sharedPref.readMessage(ACTIVATION_KEY)
        posWsRequest  = posReq
        // for update app
        POS_REQUEST = posReq
    }
}