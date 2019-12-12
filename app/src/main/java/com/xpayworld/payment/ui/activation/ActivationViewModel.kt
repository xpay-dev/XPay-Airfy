package com.xpayworld.payment.ui.activation

import android.content.Context
import android.os.Handler
import android.telephony.TelephonyManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xpayworld.payment.data.Transaction
import com.xpayworld.payment.network.PosWsRequest
import com.xpayworld.payment.network.RetrofitClient
import com.xpayworld.payment.network.activateApp.Activation
import com.xpayworld.payment.network.activateApp.ActivationApi
import com.xpayworld.payment.network.activateApp.PosInfo
import com.xpayworld.payment.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch


class ActivationViewModel(private val context: Context) : BaseViewModel() {

    //    val activateClickListener = View.OnClickListener { onClickActivate(it)}
    val navigateToEnterPin: MutableLiveData<String> = MutableLiveData()

    private lateinit var subscription: Disposable


    init {
        toolbarVisibility.value = false
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()

    }

    fun  addTransaction(){



    }

    fun callActivationAPI(code: String) {

        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val pos = PosWsRequest(context)
        pos.activationKey = code
        pos.gpsLat = "0.0"
        pos.gpsLong = "0.0"
        pos.systemMode = "Live"

        val activate = Activation()
        activate.imei = "asdasda"
        activate.ip = "192.168.1.1"
        activate.manufacturer = "Android"
        activate.model = "adas"
        activate.posWsRequest = pos
        activate.platform="android"


        val posInfo = PosInfo()
        posInfo.request = activate

        val api = RetrofitClient().getRetrofit().create(ActivationApi::class.java)

        subscription = api.activation(posInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingVisibility.value = true }
                .doAfterTerminate { loadingVisibility.value = false }
                .subscribe(
                        { result ->
                            if (!result.isSuccessful) {
                                networkError.value
                                return@subscribe
                            }

                            val hasError = result?.body()?.result?.errNumber != 0.0

                            if (hasError) {
                                requestError.value = hasError
                                Handler().postDelayed({
                                    requestError.value = !hasError
                                }, 3000)

                            } else {
                                val sharedPref = context.let { SharedPrefStorage(it) }
                                sharedPref.writeMessage(ACCOUNT_ID, result.body()!!.result.accountId!!)
                                sharedPref.writeMessage(MOBILE_APP_ID, result.body()!!.result.mobileAppId!!)
                                sharedPref.writeMessage(ACTIVATION_KEY, code)

                                navigateToEnterPin.value = ""
                            }
                        },
                        {
                            networkError.value = "Request Error"
                        }
                )}
}