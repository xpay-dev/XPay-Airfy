package com.xpayworld.payment.network.login

import android.content.Context
import com.google.gson.annotations.SerializedName

class LoginRequest {
    @SerializedName("request")
    var request : Login? = null
}