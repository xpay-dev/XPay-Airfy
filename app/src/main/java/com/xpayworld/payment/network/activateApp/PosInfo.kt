package com.xpayworld.payment.network.activateApp

import com.google.gson.annotations.SerializedName

class PosInfo  {
    @SerializedName("posInfo")
    var request : Activation = Activation()
}