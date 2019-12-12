package com.xpayworld.payment.network

import com.google.gson.annotations.SerializedName


class PosWsResponse {

    @SerializedName("AccountId")
     val accountId: String? = null

    @SerializedName("ErrNumber")
     val errNumber: Double? = null

    @SerializedName("Message")
     val message: String? = null

    @SerializedName("MobileAppId")
     val mobileAppId: String? = null


    @SerializedName("RToken")
     val rToken: String? = null

    @SerializedName("SequenceNumber")
     val sequenceNumber: String? = null

    @SerializedName("Status")
     val status: String? = null

    @SerializedName("UpdatePending")
     val updatePending: Boolean = false

}