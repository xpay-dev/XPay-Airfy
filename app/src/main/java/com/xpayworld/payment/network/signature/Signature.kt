package com.xpayworld.payment.network.signature

import com.google.gson.annotations.SerializedName
import com.xpayworld.payment.network.PosWsRequest
import com.xpayworld.payment.util.POS_REQUEST

class Signature(
    img: String = "" ,
    imgLen: String = "" ,
    mobileType: Int = 0,
    transNumber: String = ""){

        @SerializedName("Image")
        var imageStr : String? = null
        @SerializedName("ImageLen")
        var imageLenStr : String? = null
        @SerializedName("MobileAppTransType")
        var mobileAppTransType : Int? = 0
        @SerializedName("TransNumber")
        var transNumber : String? = ""
        @SerializedName("POSWSRequest")
        var posWsRequest : PosWsRequest? = null

        init {
            imageStr = img
            imageLenStr = imgLen
            mobileAppTransType = mobileType
            this.transNumber = transNumber
            posWsRequest = POS_REQUEST
        }
}