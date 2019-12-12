package com.xpayworld.payment.network.transLookUp

import com.google.gson.annotations.SerializedName
import com.xpayworld.payment.network.PosWsRequest


class TransLookUp {

    @SerializedName("AccountId")
    var accountId: String? = null

    @SerializedName("MobileAppId")
    var mobileAppId: String? = null


    @SerializedName("MobileAppTransType")
    var mobileAppTransType: Int = 0

    @SerializedName("POSWSRequest")
    var posWsRequest: PosWsRequest? = null

    @SerializedName("SearchCriteria")
    var searchCriteria: String? = null

    @SerializedName("SearchUsing")
    var searchUsing: Int = 0
}
