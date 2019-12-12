package com.xpayworld.payment.network.activateApp

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.xpayworld.payment.network.PosWsRequest
import android.content.Context.TELEPHONY_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.telephony.TelephonyManager



class Activation{

    @SerializedName("IMEI")
     var imei: String? = null

    @SerializedName("IP")
     var ip: String? = null

    @SerializedName("Manufacturer")
     var manufacturer: String? = null

    @SerializedName("Model")
     var model: String? = null

    @SerializedName("OS")
     var os: String? = null

    @SerializedName("POSWSRequest")
     var posWsRequest: PosWsRequest? = null

    @SerializedName("PhoneNumber")
     var phoneNumber: String? = null

    @SerializedName("Platform")
     var platform = "Android"
    @SerializedName("PosType")
     var postType = "BBPOS"

}