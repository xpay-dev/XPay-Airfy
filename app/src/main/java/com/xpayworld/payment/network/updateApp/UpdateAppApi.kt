package com.xpayworld.payment.network.updateApp

import com.xpayworld.payment.network.ApiConstants
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UpdateAppApi {
    @Headers(
            ApiConstants.Charset,
            ApiConstants.Content)
    @POST(ApiConstants.UpdateApp)
    fun updateApp(@Body data : UpdateAppRequest) : Single<Response<UpdateAppResult>>
}