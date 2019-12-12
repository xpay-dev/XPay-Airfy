package com.xpayworld.payment.network.transaction

import androidx.room.Entity
import java.util.*

@Entity
data class EMVCard (var data: Hashtable<String, String>) {

    var cardholderName = ""
    var emvICCData = ""
    var encTrack1 = ""
    var encTrack2 = ""
    var encTrack3 = ""
    var expiryMonth = ""
    var expiryYear = ""
    var expiryDate = "0000"
    var appId = ""
    var appReferredName = ""
    var ksn = ""
    var epb = ""
    var epbksn = ""
    var maskedPan = ""
    var serviceCode = ""
    var serialNumber = ""
    var trackEncoding = ""
    var cardNumber = ""
    var cardXNumber = ""


    init {
        cardholderName = data["cardholderName"].toString()
        expiryDate = if (data.containsKey("expiryDate")) data["expiryDate"].toString() else data["5F24"].toString()
        emvICCData = data["C2"].toString()
        ksn = if (data.containsKey("C0")) data["C0"].toString() else data["ksn"].toString()

        epbksn = ""
        maskedPan = if (data.containsKey("C4")) data["C4"].toString() else data["maskedPAN"].toString()
        appId = data["cardholderName"].toString()
        encTrack1 = data["cardholderName"].toString()
        encTrack2 = ""
        encTrack3 = ""
        serviceCode = data["cardholderName"].toString()

        cardNumber =if (data.contains("pan")) data["pan"].toString() else  data["5A"].toString()
        cardXNumber = "XXX-XXX-XXX-${maskedPan.substring(maskedPan.length - 4)}"

    }
}