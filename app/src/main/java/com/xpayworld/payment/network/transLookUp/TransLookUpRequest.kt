package com.xpayworld.payment.network.transLookUp

import com.google.gson.annotations.SerializedName
import com.xpayworld.payment.network.transaction.TransactionPurchase

class TransLookUpRequest( txn : TransLookUp) {

    @SerializedName("request")
    var request  = txn
}