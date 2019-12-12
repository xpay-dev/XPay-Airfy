package com.xpayworld.payment.network.signature

import com.xpayworld.payment.network.ApiConstants
import com.xpayworld.payment.network.TransactionResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface  SignatureApi{

    @Headers(
            ApiConstants.Charset,
            ApiConstants.Content)
    @POST(ApiConstants.TransSign)
    fun signature(@Body data:  SignatureRequest) : Single<Response<SignatureResult>>
}