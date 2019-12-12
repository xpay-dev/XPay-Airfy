package com.xpayworld.payment.network

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.xpayworld.payment.util.ACTIVATION_KEY
import com.xpayworld.payment.util.RTOKEN
import com.xpayworld.payment.util.SharedPrefStorage

class PosWsRequest(context: Context) {

    @SerializedName("ActivationKey")
     var activationKey: String? = null

    @SerializedName("GPSLat")
     var gpsLat: String? = "0.0"

    @SerializedName("GPSLong")
     var gpsLong: String? = "0.0"

    @SerializedName("RToken")
     var rToken: String? = ""

    @SerializedName("SystemMode")
     var systemMode: String? = "Live"

    init {
        val sharedPref = context.let { SharedPrefStorage(it) }
        activationKey = sharedPref.readMessage(ACTIVATION_KEY)
        rToken = sharedPref.readMessage(RTOKEN)
    }
}