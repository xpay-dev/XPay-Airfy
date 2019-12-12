package com.xpayworld.payment.network.transaction

import com.google.gson.annotations.SerializedName
import com.xpayworld.payment.network.PosWsRequest
import com.xpayworld.payment.util.paymentType
import com.xpayworld.payment.util.POS_REQUEST


class TransactionPurchase(txn: Transaction) {


    @SerializedName("AccountTypeId")
    var accountType: Int? = AccountType.NONE.ordinal

    @SerializedName("Action")
    var action: Int? = Action.NONE.ordinal

    @SerializedName("DeviceId")
    var deviceId: Int? = 7

    @SerializedName("Device")
    var device: Int? = 7

    @SerializedName("LanguageUser")
    val languageUser = "EN-CA"

    @SerializedName("ProcessOffline")
    var processOffline = false

    @SerializedName("Tips")
    var tips: Double? = 0.0

    @SerializedName("CustomerEmail")
    var customerEmail: String = ""

    @SerializedName("POSWSRequest")
    var posWsRequest: PosWsRequest? = null

    @SerializedName("DeviceModelVersion")
    var deviceModelVersion: String? = ""

    @SerializedName("DeviceOsVersion")
    var deviceOsVersion: String? = ""

    @SerializedName("PosAppVersion")
    var posAppVersion: String? = ""

    @SerializedName("CardDetails")
    var cardInfo: CardInfo? = null


    init {

        val card = CardInfo()
        card.amount = txn.amount
        card.currency = txn.currency
        card.epb = ""
        card.emvICCData = txn.card?.emvICCData ?: ""
        card.epbKsn = txn.card?.epbksn ?: ""
        card.expMonth = txn.card?.expiryMonth ?: ""
        card.expYear = txn.card?.expiryYear ?: ""
        card.isFallback = txn?.isFallback ?: false
        card.ksn = txn.card?.ksn ?: ""
        card.merchantOrderId = txn.orderId
        card.track2 = txn.card?.encTrack2 ?: ""
        card.refNumberApp = POS_REQUEST!!.activationKey +""+ System.currentTimeMillis()
        cardInfo = card
        posWsRequest = POS_REQUEST
//       posWsRequest?.systemMode = "TESTBANK"3

        when (val mPaymentType = paymentType) {
            is PaymentType.CREDIT -> {
                action = mPaymentType.action.ordinal
            }
            is PaymentType.DEBIT -> {
                accountType = mPaymentType.accountType.ordinal
            }
        }
    }


    enum class AccountType(val type: Int) {
        NONE(0), CHEQUING(1), SAVINGS(2), CURRENT(3);
        companion object : EnumCompanion<Int, AccountType>(AccountType.values().associateBy(AccountType::type))

    }

    enum class Action(val type: Int) {
        NONE(0), SWIPE(1), EMV(2);
        companion object : EnumCompanion<Int, Action>(values().associateBy(Action::type))
    }


}

open class EnumCompanion<T, V>(private val valueMap: Map<T, V>) {
    fun fromInt(type: T) = valueMap[type]
}