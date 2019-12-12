package com.xpayworld.sdk

import com.google.gson.annotations.SerializedName

class  XpayRequest {

    @SerializedName("merchant_name")
    var merchantName = ""

    @SerializedName("transaction_type")
    var transactionType = TransactionType.CREDIT.value

    @SerializedName("transaction_id")
    var transactionId = ""

    /*
    SWIPE(0),
    INSERT(1),
    TAP(2),
    SWIPE_OR_INSERT(3),
    SWIPE_OR_TAP(4),
    SWIPE_OR_INSERT_OR_TAP(5),
    INSERT_OR_TAP(6)
    */

    @SerializedName("card_capture_method")
    var cardCaptureMethod : Int = 5

    @SerializedName("amount_purchase")
    var amountPurchase = 0.00

    @SerializedName("staff_id")
    var staffId = ""

    @SerializedName("app_package_name")
    var appPackageName = ""

    @SerializedName("transaction_currency")
    var currency= ""

    @SerializedName("transaction_code")
    var currencyCode = ""

    @SerializedName("transaction_entry_point")
    var entryPoint =  EntryPoint.TRANSACTION.toString()

    @SerializedName("transaction_connection")
    var isOffine = false
}

enum class EntryPoint(val value: String) {
    TRANSACTION("TRANSACTION"),
    HISTORY("HISTORY"),
    ENTER_PIN("ENTER_PIN"),
    PREFERENCE("PREFERENCE"),
    ACTIVATION("ACTIVATION");
    override fun toString() = value
}

enum class  TransactionType(val value: Int){
    CREDIT(0),DEBIT(1)
}