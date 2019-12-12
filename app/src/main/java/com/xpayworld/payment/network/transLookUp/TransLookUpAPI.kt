package com.xpayworld.payment.network.transLookUp


import com.xpayworld.payment.network.ApiConstants
import com.xpayworld.payment.network.TransactionResponse
import io.reactivex.Observable


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface TransLookUpAPI {
    @Headers(ApiConstants.Content, ApiConstants.Charset)
    @POST(ApiConstants.TransLookUp)
    fun transLookUp(@Body body: TransLookUpRequest): Observable<Response<TransLookUpResponse>>

}
