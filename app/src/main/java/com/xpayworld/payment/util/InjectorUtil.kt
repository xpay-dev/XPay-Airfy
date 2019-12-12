package com.xpayworld.payment.util

import android.Manifest
import android.app.Activity
import android.content.Context
import com.xpayworld.payment.network.activateApp.PosInfo
import com.xpayworld.payment.ui.activation.ActivationViewModelFactory
import com.xpayworld.payment.ui.transaction.enterAmount.EnterAmountViewModelFactory
import android.Manifest.permission
import android.Manifest.permission.READ_PHONE_STATE
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.annotation.SuppressLint
import com.xpayworld.payment.data.AppDatabase
import com.xpayworld.payment.data.TransactionRepository
import com.xpayworld.payment.ui.enterPin.EnterPinModelViewModelFactory
import com.xpayworld.payment.ui.enterPin.EnterPinViewModel
import com.xpayworld.payment.ui.history.TransactionHistoryViewModelFactory
import com.xpayworld.payment.ui.link.LinkViewModel
import com.xpayworld.payment.ui.link.LinkViewModelFactory
import com.xpayworld.payment.ui.transaction.receipt.ReceiptViewModelFactory
import com.xpayworld.payment.ui.transaction.signature.SignatureViewModel
import com.xpayworld.payment.ui.transaction.signature.SignatureViewModelFactory


object InjectorUtil  {

    fun provideDialog(context: Context?): CustomDialog {
        return  CustomDialog(context!!)
    }

    fun provideEnterPinViewModelFactory(context: Context) :  EnterPinModelViewModelFactory{
        return  EnterPinModelViewModelFactory(context)
    }

    fun provideAmountViewModelFactory(context: Context): EnterAmountViewModelFactory{
        return EnterAmountViewModelFactory(context)
    }
    fun provideActivationViewModelFactor(context: Context): ActivationViewModelFactory{
        return  ActivationViewModelFactory(context)
    }

    fun  provideReceiptViewModelFactory(context: Context): ReceiptViewModelFactory{
        return  ReceiptViewModelFactory(context)
    }

    fun provideTransactionHistoryViewModel(context: Context): TransactionHistoryViewModelFactory{
        return  TransactionHistoryViewModelFactory(context)

    }

    fun provideSignatureViewModel(context: Context): SignatureViewModelFactory{
        return  SignatureViewModelFactory(context)
    }

    fun provideLinkViewModel(context: Context) : LinkViewModelFactory{
        return  LinkViewModelFactory(context)
    }

    fun getTransactionRepository(context: Context): TransactionRepository {
        return TransactionRepository.getInstance(
                AppDatabase.getDatabase(context)!!.transactionDao())
    }

}