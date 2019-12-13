package com.xpayworld.payment.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import android.util.Patterns
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.xpayworld.payment.network.PosWsRequest
import com.xpayworld.payment.network.TransactionResponse
import com.xpayworld.payment.network.transaction.PaymentType
import com.xpayworld.payment.network.transaction.Transaction
import com.xpayworld.payment.network.updateApp.UpdateAppResponse
import org.acra.ACRA.LOG_TAG
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat
import java.util.regex.Matcher
import java.util.regex.Pattern


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

fun isEmailValid(email: String): Boolean {
    val regExpn = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
    val inputStr: CharSequence = email
    val pattern: Pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
    val matcher: Matcher = pattern.matcher(inputStr)
    return matcher.matches()
}

fun isValidEmail(target: CharSequence?): Boolean {
    return if (target == null) false else Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

fun isNetworkAvailable(context: Context?): Boolean {
    val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
    return activeNetworkInfo != null
}

fun hasActiveInternetConnection(context: Context?): Boolean {
    if (isNetworkAvailable(context)) {
        try {
            val urlc: HttpURLConnection = URL("http://www.google.com").openConnection() as HttpURLConnection
            urlc.setRequestProperty("User-Agent", "Test")
            urlc.setRequestProperty("Connection", "close")
            urlc.setConnectTimeout(1500)
            urlc.connect()
            return urlc.responseCode === 200
        } catch (e: IOException) {
            Log.e(LOG_TAG, "Error checking internet connection", e)
        }
    } else {
        Log.d(LOG_TAG, "No network available!")
    }
    return false
}