package com.xpayworld.payment.ui.transaction.enterAmount

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.xpayworld.payment.network.PosWsRequest
import com.xpayworld.payment.util.*


class AmountViewModel(context: Context) : BaseViewModel() {

    val displayAmount: MutableLiveData<String> = MutableLiveData()
    val displayCurrency: MutableLiveData<String> =  MutableLiveData()
    val okClickListener = View.OnClickListener { onClickOk(it) }
    val deviceError : MutableLiveData<Pair<String,String>> = MutableLiveData()
    val btnPayEnabled: MutableLiveData<Boolean> = MutableLiveData()
    //GG7C-BY7B-JF5A-HNN7
    val amountStr : MutableLiveData<String> = MutableLiveData()
    val navigateToEnterPin: MutableLiveData<Boolean> = MutableLiveData()
    val navigateToActivation: MutableLiveData<Boolean> = MutableLiveData()
    val sharedPref = SharedPrefStorage(context)

    init {
        displayCurrency.value = "PHP"
        displayAmount.value = "0.00"
        btnPayEnabled.value = false
        POS_REQUEST = PosWsRequest(context)
        isActivated()
        isPinEntered()
    }

    private fun onClickOk(v: View) {
        if (!isDeviceAvailable()) {
            deviceError.value = Pair("No Device found","Please go to preference to pair device")
            return
        }
        if (amountStr.value!!.isEmpty()) return
        transaction.amount = ( amountStr.value!!.toInt()/100.0)

        val directions = PayAmountFragmentDirections.navigateToProcessTransactionFragment(amountStr.value!!)
        v.findNavController().navigate(directions)

    }

    private fun isActivated(){
        navigateToActivation.value = sharedPref.isEmpty(ACTIVATION_KEY)
    }

    private  fun isPinEntered(){
        val hasPin = !sharedPref.isEmpty(ACTIVATION_KEY) &&  sharedPref.isEmpty(PIN_LOGIN)
        navigateToEnterPin.value = hasPin
    }

    private fun isDeviceAvailable() : Boolean{
        return !sharedPref.isEmpty(WISE_PAD) || !sharedPref.isEmpty(WISE_POS)
    }
}

