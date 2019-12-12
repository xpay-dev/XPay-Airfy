package com.xpayworld.sdk

import com.google.gson.annotations.SerializedName

class  XpayResponse {
    @SerializedName("response")
    var response  = ""

    @SerializedName("card_number")
    var cardNumber = ""

    @SerializedName("masked_card")
    var maskedCard = ""

    @SerializedName("card_expiry")
    var expiry= ""

    @SerializedName("response_message")
    var responseMsg = ""
}