package com.xpayworld.payment.network.transaction

import com.google.gson.annotations.SerializedName
import com.xpayworld.payment.network.TransactionResponse

class TransactionResult {

    @SerializedName("TransactionPurchaseCreditEMVResult")
    var resultEmv :  TransactionResponse? = null

    @SerializedName("TransactionPurchaseCreditSwipeResult")
    var resultSwipe :  TransactionResponse? = null
 }