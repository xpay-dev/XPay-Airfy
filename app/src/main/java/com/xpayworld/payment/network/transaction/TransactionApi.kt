package com.xpayworld.payment.network.transaction

import com.xpayworld.payment.network.ApiConstants
import com.xpayworld.payment.network.TransactionResponse
import com.xpayworld.payment.network.activateApp.ActivateAppResult
import com.xpayworld.payment.network.activateApp.PosInfo
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TransactionApi {

    @Headers(
            ApiConstants.Charset,
            ApiConstants.Content)
    @POST(ApiConstants.TransCreditEMV)
    fun creditEMV(@Body  data : TransactionRequest) : Single<Response<TransactionResult>>

    @Headers(
            ApiConstants.Charset,
            ApiConstants.Content)
    @POST(ApiConstants.TransCreditSWIPE)
    fun creditSwipe(@Body  data : TransactionRequest) : Single<Response<TransactionResult>>

}