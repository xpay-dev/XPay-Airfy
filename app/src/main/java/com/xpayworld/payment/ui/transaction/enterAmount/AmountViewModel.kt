package com.xpayworld.payment.ui.transaction.enterAmount

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.xpayworld.payment.network.PosWsRequest
import com.xpayworld.payment.network.RetrofitClient
import com.xpayworld.payment.network.TransactionResponse
import com.xpayworld.payment.network.login.Login
import com.xpayworld.payment.network.login.LoginApi
import com.xpayworld.payment.network.login.LoginRequest
import com.xpayworld.payment.network.transaction.*
import com.xpayworld.payment.ui.history.DispatchGroup
import com.xpayworld.payment.ui.history.convertLongToTime
import com.xpayworld.payment.util.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


class AmountViewModel(context: Context) : BaseViewModel() {

    val displayAmount: MutableLiveData<String> = MutableLiveData()
    val displayCurrency: MutableLiveData<String> = MutableLiveData()
    val okClickListener = View.OnClickListener { onClickOk(it) }
    val deviceError: MutableLiveData<Pair<String, String>> = MutableLiveData()
    val btnPayEnabled: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var subscription: Disposable
    val amountStr: MutableLiveData<String> = MutableLiveData()
    val navigateToEnterPin: MutableLiveData<Boolean> = MutableLiveData()
    val navigateToActivation: MutableLiveData<Boolean> = MutableLiveData()
    val sharedPref = SharedPrefStorage(context)

    init {
        displayCurrency.value = "PHP"
        displayAmount.value = "0.00"
        btnPayEnabled.value = false
        POS_REQUEST = PosWsRequest(context)

        isActivated()

        val pinEntered = isPinEntered()
        if (pinEntered) {
            hasInternetConnection(context)
        }
    }

    private fun onClickOk(v: View) {
        if (!isDeviceAvailable()) {
            deviceError.value = Pair("No Device found", "Please go to preference to pair device")
            return
        }
        if (amountStr.value!!.isEmpty()) return
        transaction.amount = (amountStr.value!!.toInt() / 100.0)

        val directions = PayAmountFragmentDirections.navigateToProcessTransactionFragment(amountStr.value!!)
        v.findNavController().navigate(directions)

    }

    private fun isActivated() {
        val isValid = sharedPref.isEmpty(ACTIVATION_KEY)
        navigateToActivation.value = isValid
    }

    private fun isPinEntered(): Boolean {
        val hasPin = !sharedPref.isEmpty(ACTIVATION_KEY) || !sharedPref.isEmpty(PIN_LOGIN)
        navigateToEnterPin.value = hasPin
        return  hasPin
    }

    private fun isDeviceAvailable(): Boolean {
        return !sharedPref.isEmpty(WISE_PAD) || !sharedPref.isEmpty(WISE_POS)
    }

    private fun hasInternetConnection(context: Context) {
        if (isNetworkAvailable(context)) {
            callEnterPinAPI(context)
        }
    }


    private fun callEnterPinAPI(context: Context) {
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
                .doAfterTerminate { loadingVisibility.value = false }
                .subscribe(
                        { result ->
                            if (!result.isSuccessful) {
                                networkError.value = "Network Error ${result.code()}"
                                return@subscribe
                            }
                            val hasError = result?.body()?.result?.errNumber != 0.0

                            if (hasError) {
                                loadingVisibility.value = false
                                requestError.value = hasError

                            } else {
                                val sharedPref = context.let { SharedPrefStorage(it) }
                                sharedPref.writeMessage(RTOKEN, result.body()!!.result.rToken!!)
                                callforBatchUpload(context)

                            }
                        },
                        {
                            loadingVisibility.value = false
                            networkError.value = "Network Error"
                        })
    }

    private fun callTransactionAPI( callBack: ((Boolean) -> Unit)? = null) {
        var txnResponse: Single<Response<TransactionResult>>? = null
        val api = RetrofitClient().getRetrofit().create(TransactionApi::class.java)

        val txnPurchase = TransactionPurchase(transaction)
        // attached transaction date if offline
        if (transaction.timestamp != 0L){
            txnPurchase.cardInfo?.refNumberApp = POS_REQUEST?.activationKey +""+ transaction.timestamp
        }

        when (val mPaymentType = paymentType) {
            is PaymentType.DEBIT -> {

            }
            is PaymentType.CREDIT -> {
                if (mPaymentType.action != TransactionPurchase.Action.SWIPE) {
                    txnResponse = api.creditEMV(TransactionRequest(txnPurchase))
                } else {
                    txnResponse = api.creditSwipe(TransactionRequest(txnPurchase))
                }
            }
        }

        subscription = txnResponse!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {loadingVisibility.value = true }
                .doAfterTerminate {loadingVisibility.value = false }
                .subscribe({ result ->
                    if (!result.isSuccessful) {
                        subscription.dispose()
                        callBack?.invoke(false)
                        return@subscribe
                    }
                    val body = if ( result.body()?.resultEmv != null) result.body()?.resultEmv else  result.body()?.resultSwipe
                    val hasError = body?.result?.errNumber != 0.0
                    callBack?.invoke(!hasError)
                    subscription.dispose()
                }, {
                    callBack?.invoke(false)
                    subscription.dispose()
                })
    }

    fun callforBatchUpload(context: Context){

        val txnDao = InjectorUtil.getTransactionRepository(context)
        val dispatch = DispatchGroup()
        loadingVisibility.value = true
        for (txn in txnDao.getTransaction()) {
            if (!txn.isSync){
                dispatch.enter()
                txnDao.updateTransaction(true,txn.orderId)
                val trans = Transaction()

                trans.card = txn.card
                trans.orderId = txn.orderId
                trans.isOffline = txn.isOffline
                trans.amount = txn.amount
                trans.currencyCode = txn.currencyCode
                trans.currency = txn.currency
                trans.timestamp = txn.timestamp


                if (trans.card?.posEntry == 90) {
                    trans.paymentType = PaymentType.CREDIT(TransactionPurchase.Action.SWIPE)
                } else {
                    trans.paymentType = PaymentType.CREDIT(TransactionPurchase.Action.EMV)
                }

                trans.device = txn.device
                trans.deviceModelVersion = txn.deviceModelVersion

                transaction = trans

                callTransactionAPI(callBack = {isSuccess ->
                    if (!isSuccess) {
                        txnDao.updateTransaction(false, trans.orderId)
                    }
                    dispatch.leave()
                })
            }
        }
        dispatch.notify {
            loadingVisibility.value = false
        }
    }

}

