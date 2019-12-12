package com.xpayworld.payment.network.updateApp

import com.google.gson.annotations.SerializedName
import com.xpayworld.payment.network.PosWsResponse

class UpdateAppResponse  {

    @SerializedName("Amount1")
    var amount1 : Double? = null

    @SerializedName("Amount2")
    var amount2 : Double? = null

    @SerializedName("BillingCyclesCheckEmail")
    var billingCyclesCheckEmail : Double? = null

    @SerializedName("BillingCyclesCheckPrint")
    var billingCyclesCheckPrint : Double? = null

    @SerializedName("BillsPayment")
    var billsPayment : Boolean? = null

    @SerializedName("CashSignature")
    var cashSignature : Boolean? = null

    @SerializedName("CashTransaction")
    var cashTransaction : Boolean? = null

    @SerializedName("CheckForEmailDuplicates")
    var checkForEmailDuplicates : Boolean? = null

    @SerializedName("CheckForPrintDuplicates")
    var checkForPrintDuplicates : Boolean? = null

    @SerializedName("ChequeProofId")
    var chequeProofId : Boolean? = null

    @SerializedName("ChequeSignature")
    var chequeSignature : Boolean? = null

    @SerializedName("ChequeTransaction")
    var chequeTransaction : Boolean? = null

    @SerializedName("ChequeType")
    var chequeType : Boolean? = null

    @SerializedName("Countries")
    var countries : List<Country>? = null

    @SerializedName("CreditSignature")
    var creditSignature : Boolean? = null

    @SerializedName("CreditTransaction")
    var creditTransaction : Boolean? = null

    @SerializedName("Currency")
    var currency : String? = null

    @SerializedName("DebitBalanceCheck")
    var debitBalanceCheck : Boolean? = null

    @SerializedName("DebitRefund")
    var debitRefund : Boolean? = null

    @SerializedName("DebitSignature")
    var debitSignature : Boolean? = null

    @SerializedName("DebitTransaction")
    var debitTransaction : Boolean? = null

    @SerializedName("Devices")
    var devices : List<Device>? = null

    @SerializedName("Disclaimer")
    var disclamier : String? = null

    @SerializedName("DisclaimerRequired")
    var disclaimerRequired : Boolean? = null

    @SerializedName("EmailAllowed")
    var emailAllowed : Boolean? = null

    @SerializedName("EmailLimit")
    var emailLimit : Int? = null

    @SerializedName("GPSEnabled")
    var gpsEnabled : Boolean? = null

    @SerializedName("GiftAllowed")
    var giftAllowed : Boolean? = null

    @SerializedName("HelpClosedHour")
    var helpClosedHour : String? = null

    @SerializedName("HelpContactNumber")
    var helpContactNumber : String? = null

    @SerializedName("HelpGMT")
    var helpGMT : String? = null

    @SerializedName("HelpOpenHour")
    var helpOpenHour : String? = null

    @SerializedName("MerchantDetails")
    var merchantDetails : MerchantDetails? = null

    @SerializedName("POSWSResponse")
    var result : PosWsResponse? = null

    @SerializedName("Percentage1")
    var percentage1 : Int? = null

    @SerializedName("Percentage2")
    var percentage2 : Int? = null

    @SerializedName("Percentage3")
    var percentage3 : Int? = null

    @SerializedName("PrintAllowed")
    var printAllowed : Boolean? = null

    @SerializedName("PrintLimit")
    var printLimit : Int? = null

    @SerializedName("ReferenceNumber")
    var referenceNumber : Boolean? = null

    @SerializedName("SMSEnabled")
    var smsEnabled : Boolean? = null

    @SerializedName("SystemMode")
    var systemMode : String? = null

    @SerializedName("TermsOfService")
    var termsOfService : String? = null

    @SerializedName("TipsEnabled")
    var tipsEnabled : Boolean? = null
}

class Country {
    @SerializedName("CountryCode")
    var code : String? = null
    @SerializedName("CountryName")
    var name : String? = null
}

class Device {
    @SerializedName("DeviceFlowType")
    var flowType : String? = null
    @SerializedName("DeviceFlowTypeId")
    var flowTypeId : Int? = null
    @SerializedName("DeviceId")
    var Id : Int? = null
    @SerializedName("DeviceType")
    var type : String? = null
    @SerializedName("MasterDeviceId")
    var masterDeviceId : Int? = null
    @SerializedName("SerialNumber")
    var serialNumber : String? = null
}

class MerchantDetails {
    @SerializedName("Address")
    var address : String? = null

    @SerializedName("AppLanguage")
    var appLanguage : String? = null

    @SerializedName("BranchName")
    var branchName : String? = null

    @SerializedName("City")
    var city : String? = null

    @SerializedName("Country")
    var country : String? = null

    @SerializedName("DisclaimerEnabled")
    var disclaimerEnabled : Boolean? = null

    @SerializedName("MerchantId")
    var merchantId : Int? = null

    @SerializedName("MerchantName")
    var merchantName : String? = null

    @SerializedName("PrimaryContactNumber")
    var primaryContactNumber : String? = null

    @SerializedName("State")
    var state : String? = null

    @SerializedName("TOSEnabled")
    var tosEnabled : Boolean? = null

    @SerializedName("Zip")
    var zip : Boolean? = null
}