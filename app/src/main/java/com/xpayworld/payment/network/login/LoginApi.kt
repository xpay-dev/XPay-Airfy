package com.xpayworld.payment.network.login

import com.xpayworld.payment.network.ApiConstants
import com.xpayworld.payment.network.PosWsResponse
import com.xpayworld.payment.network.activateApp.Activation
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface  LoginApi {
    @Headers(
            ApiConstants.Charset,
            ApiConstants.Content)
    @POST(ApiConstants.Login)
    fun login(@Body login : LoginRequest) : Observable<Response<LoginResult>>
}