package com.xpayworld.payment.network.transaction

import com.google.gson.annotations.SerializedName
import com.xpayworld.payment.network.activateApp.Activation

class TransactionRequest( txn : TransactionPurchase) {

    @SerializedName("request")
    var request  = txn
}