package com.xpayworld.payment.network.updateApp

import com.google.gson.annotations.SerializedName
import com.xpayworld.payment.network.PosWsRequest
import com.xpayworld.payment.util.POS_REQUEST

class UpdateAppRequest {
    @SerializedName("request")
    var request  : PosWsRequest? = null

    init {
        request = POS_REQUEST
    }

}