package com.xpayworld.payment.network.activateApp


import com.xpayworld.payment.network.ApiConstants
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface  ActivationApi {
    @Headers(
            ApiConstants.Charset,
            ApiConstants.Content)
    @POST(ApiConstants.ActivateApp)
   fun activation(@Body activate : PosInfo) : Single<Response<ActivateAppResult>>
}