package com.xpayworld.payment.ui.link

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.xpayworld.payment.network.RetrofitClient
import com.xpayworld.payment.network.login.Login
import com.xpayworld.payment.network.login.LoginApi
import com.xpayworld.payment.network.login.LoginRequest
import com.xpayworld.payment.network.updateApp.UpdateAppApi
import com.xpayworld.payment.network.updateApp.UpdateAppRequest
import com.xpayworld.payment.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LinkViewModel (private val context : Context) : BaseViewModel(){


    private lateinit var subscription: Disposable
    val navigateToNextEntry : MutableLiveData<Boolean> = MutableLiveData()

    init {
        toolbarVisibility.value = false
        callEnterPinAPI()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun callUpdateApp(callback: (()-> Unit)? = null){
        val api = RetrofitClient().getRetrofit().create(UpdateAppApi::class.java)

        val request = UpdateAppRequest()
        subscription = api.updateApp(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {}
                .doAfterTerminate { loadingVisibility.value = false }
                .subscribe({ result ->
                    merchantDetails = result.body()?.result?.merchantDetails
                    callback?.invoke()
                },{
                    println("Errorr"+it)
                })
    }

    fun callEnterPinAPI() {
        val sharedPref = context.let { SharedPrefStorage(it) }
        val api = RetrofitClient().getRetrofit().create(LoginApi::class.java)
        val login = Login(context)
        login.appVersion = "1"
        login.pin = sharedPref.readMessage(PIN_LOGIN)

        val request = LoginRequest()
        request.request = login

        subscription = api.login(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingVisibility.value = true }
                .doAfterTerminate {}
                .subscribe(
                        { result ->
                            if (!result.isSuccessful) {
                                networkError.value = "Network Error ${result.code()}"

                                return@subscribe
                            }
                            val hasError = result?.body()?.result?.errNumber != 0.0

                            if (hasError) {
                                loadingVisibility.value = false

                                requestError.value = result?.body()?.result

                            } else {

                                val rToken =  result.body()!!.result.rToken!!
                                sharedPref.writeMessage(RTOKEN,rToken)
                                POS_REQUEST?.rToken = rToken

                                callUpdateApp(callback = {
                                    navigateToNextEntry.value = true
                                })
                            }
                        },
                        {
                            loadingVisibility.value = false

                            networkError.value = "Network Error"
                        }
                )
    }
}