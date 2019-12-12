package com.xpayworld.payment.network.signature

import com.google.gson.annotations.SerializedName

class  SignatureRequest {

    @SerializedName("request")
    var request: Signature? = null
}