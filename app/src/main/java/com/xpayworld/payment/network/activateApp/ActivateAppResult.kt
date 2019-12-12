package com.xpayworld.payment.network.activateApp

import com.google.gson.annotations.SerializedName
import com.xpayworld.payment.network.PosWsResponse

class  ActivateAppResult {
    @SerializedName("ActivateAppResult")
    var result = PosWsResponse()
}