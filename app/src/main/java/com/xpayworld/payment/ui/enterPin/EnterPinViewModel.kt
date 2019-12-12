package com.xpayworld.payment.ui.enterPin

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import com.xpayworld.payment.data.Transaction
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
import kotlinx.android.synthetic.main.fragment_activation_code.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EnterPinViewModel(private val context: Context) : BaseViewModel() {

    val clearClickListener = View.OnClickListener { onClickClear(it) }
    val numpadClickListener = View.OnClickListener { onClickNumpad(it) }

    val pinCode: MutableLiveData<String> = MutableLiveData()
    val navigateToEnterAmount: MutableLiveData<String> = MutableLiveData()

    private lateinit var subscription: Disposable

    init {
        toolbarVisibility.value = false
        pinCode.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    private fun onClickNumpad(v: View?) {
        if (pinCode.value!!.length >= 4) return
        pinCode.value += (v as Button).text.toString()
    }

    private fun onClickClear(v: View?) {
        pinCode.value = pinCode.value?.dropLast(1)
    }

    fun callUpdateAp(callback: (()-> Unit)? = null){
        val api = RetrofitClient().getRetrofit().create(UpdateAppApi::class.java)

        val request = UpdateAppRequest()
        subscription = api.updateApp(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {loadingVisibility.value = true }
                .doAfterTerminate { loadingVisibility.value = false }
                .subscribe({ result ->
                    merchantDetails = result.body()?.result?.merchantDetails
                    callback?.invoke()
                },{
                    println("Errorr"+it)
                })
    }

    fun callEnterPinAPI() {
        val api = RetrofitClient().getRetrofit().create(LoginApi::class.java)
        val login = Login(context)
        login.appVersion = "1"
        login.pin = pinCode.value!!

        val request = LoginRequest()
        request.request = login

        subscription = api.login(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingVisibility.value = true }
                .doAfterTerminate {loadingVisibility.value = false}
                .subscribe(
                        { result ->
                            if (!result.isSuccessful) {
                                networkError.value = "Network Error ${result.code()}"
                                pinCode.value = ""
                                return@subscribe
                            }
                            val hasError = result?.body()?.result?.errNumber != 0.0

                            if (hasError) {
                                loadingVisibility.value = false
                                pinCode.value = ""
                                requestError.value = hasError

                            } else {
                                val sharedPref = context.let { SharedPrefStorage(it) }
                                sharedPref.writeMessage(RTOKEN,result.body()!!.result.rToken!!)
                                sharedPref.writeMessage(PIN_LOGIN,pinCode.value!!)

                                callUpdateAp(callback = {
                                    toolbarVisibility.value = true
                                    navigateToEnterAmount.value = ""
                                })
                            }
                        },
                        {
                            loadingVisibility.value = false
                            pinCode.value = ""
                            networkError.value = "Network Error"
                        }
                )
    }
}


