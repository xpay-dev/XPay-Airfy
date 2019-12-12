package com.xpayworld.payment.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.xpayworld.payment.network.PosWsRequest
import com.xpayworld.payment.network.TransactionResponse
import com.xpayworld.payment.network.transaction.PaymentType
import com.xpayworld.payment.network.transaction.Transaction
import com.xpayworld.payment.network.updateApp.UpdateAppResponse
import com.xpayworld.sdk.XpayResponse
import java.text.DecimalFormat

// Global Variables

var paymentType : PaymentType? = null
var POS_REQUEST  : PosWsRequest? = null
var transactionResponse : TransactionResponse? = null
var transaction =  Transaction()
var merchantDetails = UpdateAppResponse().merchantDetails
var IS_SDK = false
var IS_TRANSACTION_OFFLINE = false
var externalPackageName = ""
var SDK_XPAY_RESPONSE = ""


fun formattedAmount(amount : String) : String {
    var formatedAmount = ""

    if (amount == "" || amount.run { isNullOrBlank() }) {return  "0.00"}

    val len = amount.length
    val df = DecimalFormat("###,###,##0.00")
    if (len in 1..8) {
        val s = String.format("%6.2f", amount.toInt() / 100.0)
        formatedAmount = df.format(s.toDouble())
    } else if (len == 0) {
        formatedAmount = "0.00"
    }
    return formatedAmount
}

@SuppressLint("HardwareIds")
fun getDeviceIMEI(activity: Activity): String? {
    var deviceUniqueIdentifier: String? = null
    val tm = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
         //   imei = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }
    } else {
      //  imei = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }
    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_PHONE_STATE), 1)
    else
//        deviceUniqueIdentifier = tm.deviceId
    if (null == deviceUniqueIdentifier || deviceUniqueIdentifier.isEmpty())
        deviceUniqueIdentifier = "0"
    return deviceUniqueIdentifier
}



