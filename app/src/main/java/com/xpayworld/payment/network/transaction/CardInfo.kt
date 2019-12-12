package com.xpayworld.payment.network.transaction

import com.google.gson.annotations.SerializedName

class CardInfo {

    @SerializedName("Amount")
    var amount = 0.0

    @SerializedName("CardNumber")
    var cardNumber = ""

    @SerializedName("Currency")
    var currency = ""

    @SerializedName("EmvICCData")
    var emvICCData = ""

    @SerializedName("Epb")
    var epb: String? = null

    @SerializedName("EpbKsn")
    var epbKsn: String? = null

    @SerializedName("ExpMonth")
    var expMonth = ""

    @SerializedName("ExpYear")
    var expYear = ""

    @SerializedName("IsFallback")
    var isFallback = false

    @SerializedName("Ksn")
    var ksn = ""

    @SerializedName("MerchantOrderId")
    var merchantOrderId = ""

    @SerializedName("NameOnCard")
    var nameOnCard = ""

    @SerializedName("RefNumberApp")
    var refNumberApp = ""

    @SerializedName("Track1")
    var track1 = ""
    @SerializedName("Track2")
    var track2 = ""

    @SerializedName("Track3")
    var track3 = ""

}