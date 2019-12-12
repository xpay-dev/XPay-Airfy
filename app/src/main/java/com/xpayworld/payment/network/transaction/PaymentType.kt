package com.xpayworld.payment.network.transaction

import androidx.room.Entity
import kotlinx.android.parcel.RawValue

@Entity
sealed class PaymentType {

    data class  CREDIT(var  action : TransactionPurchase.Action) : PaymentType()
    data class  DEBIT(var debit : DebitTransaction, var accountType: TransactionPurchase.AccountType): PaymentType()
    object CASH : PaymentType()

    enum class DebitTransaction(val rawValue : Int, var stringValue : String ){
        SALE(0, "Sale"),BALANCEINQUIRY(1, "Balance Inquiry")
    }
}