package com.xpayworld.payment.ui.transaction.processTransaction

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.bbpos.bbdevice.BBDeviceController
import com.bbpos.bbdevice.BBDeviceController.*
import com.bbpos.bbdevice.CAPK
import com.bbpos.bbdevice.ota.BBDeviceOTAController
import com.xpayworld.payment.R
import com.xpayworld.payment.data.CardData
import com.xpayworld.payment.network.transaction.PaymentType
import com.xpayworld.payment.network.transaction.TransactionPurchase
import com.xpayworld.payment.ui.base.kt.BaseFragment
import com.xpayworld.payment.ui.preference.DeviceAdapter
import com.xpayworld.payment.ui.transaction.receipt.ReceiptFragment
import com.xpayworld.payment.util.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


const val ARG_AMOUNT = "amount"
abstract class BaseDeviceFragment : BaseFragment()  {

    companion object {
        lateinit var navHostFragment : Fragment
        lateinit var currentFragment : Fragment
        lateinit var amountStr : String
        lateinit var pinButtonLayout: Hashtable<String, Rect>
    }

    // Preference Fragment
    private val DEVICE_NAMES = arrayOf("WP")
    private val checkCardMode: CheckCardMode? = null

    internal var bbDeviceController: BBDeviceController? = null
    internal lateinit var otaController: BBDeviceOTAController
    private var listener: MyBBdeviceControllerListener? = null
    var cardData =  CardData()

    val toolbarTitle: MutableLiveData<String> = MutableLiveData()
    val startAnimation: MutableLiveData<Boolean> = MutableLiveData()

    val cancelVisibility : MutableLiveData<Int> = MutableLiveData()
    val cancelTitle : MutableLiveData<String> = MutableLiveData()
    val checkBluetoothPermission : MutableLiveData<Boolean> = MutableLiveData()
    val deviceListAdapter  = DeviceAdapter()
    var deviceArr : MutableList<BluetoothDevice> = arrayListOf()

    // Process Transaction Fragment
    val proceedTransaction : MutableLiveData<Boolean> = MutableLiveData()
    val onTransactionResult : MutableLiveData<Boolean> = MutableLiveData()
    val onlineProcessResult : MutableLiveData<String> = MutableLiveData()
    val onReceiptData: MutableLiveData<ByteArray> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            amountStr = it.getString(ARG_AMOUNT).toString()
        }

        navHostFragment = activity!!.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
        currentFragment = navHostFragment.childFragmentManager.fragments[0]

        listener = MyBBdeviceControllerListener()
        bbDeviceController = getInstance(currentFragment.requireContext(), listener)
        setDebugLogEnabled(true)

        if (currentFragment is ProcessTransactionFragment){
            if (!SharedPrefStorage(requireActivity()).isEmpty(WISE_PAD) && isBluetoothPermissionGranted()){
                startBluetoothConnection()
            } else if (!SharedPrefStorage(requireActivity()).isEmpty(WISE_POS)){
                startTransaction()
            } else {
                checkBluetoothPermission.value = false
            }
        }
        else if (currentFragment is ReceiptFragment){
            bbDeviceController?.startSerial()
        }

    }


    fun startTransaction() {
        cancelVisibility.value = View.INVISIBLE
        if (bbDeviceController?.connectionMode == ConnectionMode.SERIAL) return
        bbDeviceController?.startSerial()
        cancelVisibility.value = View.VISIBLE
        toolbarTitle.value = "Initializing..."
    }

    fun startBluetoothConnection(){
        toolbarTitle.value = "Initializing..."
        bbDeviceController?.startBTScan(DEVICE_NAMES, 120)
    }

    private fun startEmv() {
        val data: Hashtable<String, Any> = Hashtable() //define empty hashmap

        data["emvOption"] = BBDeviceController.EmvOption.START
        data["orderID"] = "0123456789ABCDEF0123456789ABCDEF"
        data["randomNumber"] = "012345"

        // Terminal Time
        val terminalTime = SimpleDateFormat("yyMMddHHmmss").format(Calendar.getInstance().time)
        data["terminalTime"] = terminalTime

        // Check for Swipe, Insert and Tap card
        //BBDeviceController.CheckCardMode.SWIPE_OR_INSERT_OR_TAP  transaction.cardCaptureMethod
//        SWIPE(0),
//        INSERT(1),
//        TAP(2),
//        SWIPE_OR_INSERT(3),
//        SWIPE_OR_TAP(4),
//        SWIPE_OR_INSERT_OR_TAP(5),
//        INSERT_OR_TAP(6)


        var checkCardMode =  CheckCardMode.SWIPE_OR_INSERT_OR_TAP
        when(transaction.cardCaptureMethod){
            0-> {
                checkCardMode =  CheckCardMode.SWIPE
            }
            1->{
                checkCardMode =  CheckCardMode.INSERT
            }
            2-> {
                checkCardMode =  CheckCardMode.TAP
            }
            3-> {
                checkCardMode =   CheckCardMode.SWIPE_OR_INSERT
            }
            4->{
                checkCardMode =   CheckCardMode.SWIPE_OR_TAP
            }
            5->{
                checkCardMode =  CheckCardMode.SWIPE_OR_INSERT_OR_TAP
            }
            6->{
                checkCardMode =   CheckCardMode.INSERT_OR_TAP
            }
        }


        data["checkCardMode"] = checkCardMode
        startAnimation.value = true
        toolbarTitle.value = "Please confirm amount"
        bbDeviceController!!.startEmv(data)
    }

    private fun promptForAmount() {
        val input: Hashtable<String, Any> = Hashtable() //define empty hashmap
        // Amount
        var currencyCharacters = listOf(BBDeviceController.CurrencyCharacter.P, BBDeviceController.CurrencyCharacter.H, BBDeviceController.CurrencyCharacter.P)
        input.put("amount", String.format("%.2f", amountStr.toInt() / 100.0))
        input.put("transactionType", BBDeviceController.TransactionType.GOODS)
        input.put("currencyCode", transaction.currencyCode)
        bbDeviceController?.setAmount(input)
    }


    fun stopConnection() {
        when (bbDeviceController!!.connectionMode) {
            ConnectionMode.BLUETOOTH -> {
                bbDeviceController?.disconnectBT()
            }
            ConnectionMode.AUDIO -> {
                bbDeviceController?.stopAudio()
            }
            ConnectionMode.SERIAL -> {
                bbDeviceController?.stopSerial()
            }
            ConnectionMode.USB -> {
                bbDeviceController?.stopUsb()
            }
        }
        deviceArr.clear()
        deviceListAdapter.notifyDataSetChanged()
    }

    fun isBluetoothPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(context!!, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context!!, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED
    }

    private inner class MyBBdeviceControllerListener : BBDeviceController.BBDeviceControllerListener {
        override fun onReturnUpdateAIDResult(p0: Hashtable<String, BBDeviceController.TerminalSettingStatus>?) {

        }

        override fun onReturnAccountSelectionResult(p0: BBDeviceController.AccountSelectionResult?, p1: Int) {

        }

        override fun onReturnEmvCardNumber(p0: Boolean, p1: String?) {

        }

        override fun onDeviceDisplayingPrompt() {

        }

        override fun onRequestSelectApplication(p0: ArrayList<String>?) {
            bbDeviceController?.selectApplication(0)
        }

        override fun onRequestDisplayText(displayText: BBDeviceController.DisplayText?) {

            toolbarTitle.value = displayText.toString()

        }

        override fun onReturnPrintResult(p0: BBDeviceController.PrintResult?) {

        }

        override fun onReturnDisableAccountSelectionResult(p0: Boolean) {

        }

        override fun onReturnAmount(p0: Hashtable<String, String>?) {

        }

        override fun onBTConnected(pairedObjects: BluetoothDevice?) {
            if (currentFragment is ProcessTransactionFragment)
              startEmv()
        }

        override fun onReturnApduResult(p0: Boolean, p1: Hashtable<String, Any>?) {

        }

        override fun onReturnCAPKDetail(p0: CAPK?) {

        }

        override fun onReturnReversalData(p0: String?) {

        }

        override fun onReturnReadAIDResult(p0: Hashtable<String, Any>?) {

        }

        override fun onRequestOnlineProcess(tlv: String?) {
            val decodeData = BBDeviceController.decodeTlv(tlv)

            println(decodeData["C0"])
            println(tlv)

            println("ksn : ${decodeData["C0"]}")
            println("emvICCData : ${decodeData["C2"]}")
            println("maskedPan : ${decodeData["C4"]}")
            println("PAN : ${decodeData["5A"]}")


            cardData.emvICCData =  decodeData["C2"].toString()
            cardData.expiryDate =  decodeData["5F24"].toString()
            cardData.ksn = decodeData["C0"].toString()
            cardData.cardNumber = decodeData["5A"].toString()
            cardData.cardXNumber = decodeData["C4"].toString()

//

            transaction.card = cardData
            proceedTransaction.value =  true

            onlineProcessResult.observe(currentFragment ,androidx.lifecycle.Observer {
                bbDeviceController?.sendOnlineProcessResult(it)
                //8A023030
            })
        }

        override fun onReturnNfcDataExchangeResult(p0: Boolean, p1: Hashtable<String, String>?) {

        }

        override fun onBTScanStopped() {
            toolbarTitle.value = "Bluetooth scan stopped"

        }

        override fun onReturnPowerOnIccResult(p0: Boolean, p1: String?, p2: String?, p3: Int) {

        }

        override fun onReturnEmvReport(p0: String?) {

        }

        override fun onReturnPhoneNumber(p0: BBDeviceController.PhoneEntryResult?, p1: String?) {

        }

        override fun onReturnCAPKLocation(p0: String?) {

        }

        override fun onReturnEncryptPinResult(p0: Boolean, p1: Hashtable<String, String>?) {


        }

        override fun onAudioDeviceUnplugged() {

        }

        override fun onSessionError(p0: BBDeviceController.SessionError?, p1: String?) {

        }

        override fun onAudioAutoConfigCompleted(p0: Boolean, p1: String?) {

        }

        override fun onReturnEmvCardDataResult(p0: Boolean, p1: String?) {

        }

        override fun onReturnReadGprsSettingsResult(p0: Boolean, p1: Hashtable<String, Any>?) {}

        override fun onRequestClearDisplay() {}

        override fun onRequestTerminalTime() {}

        override fun onAudioAutoConfigError(p0: BBDeviceController.AudioAutoConfigError?) {}

        override fun onReturnReadTerminalSettingResult(p0: Hashtable<String, Any>?) {}

        override fun onReturnVasResult(p0: BBDeviceController.VASResult?, p1: Hashtable<String, Any>?) {}

        override fun onReturnControlLEDResult(p0: Boolean, p1: String?) {}

        override fun onReturnRemoveCAPKResult(p0: Boolean) {}

        override fun onBTReturnScanResults(foundDevices: MutableList<BluetoothDevice>?) {
            deviceArr = foundDevices!!

            if (currentFragment is ProcessTransactionFragment){

                foundDevices.forEach { device ->
                    if (device.name == SharedPrefStorage(context!!).readMessage(WISE_PAD)) {
                        bbDeviceController!!.connectBT(device)
                    }
                }
            }
            else {
                deviceListAdapter.updatePostList(deviceArr)
            }
        }

        override fun onEnterStandbyMode() {

        }

        override fun onPrintDataEnd() {

        }

        override fun onReturnDisableInputAmountResult(p0: Boolean) {

        }

        override fun onSerialDisconnected() {

        }


        override fun onBTRequestPairing() {
            toolbarTitle.value = "Request Pairing"
        }

        override fun onDeviceHere(p0: Boolean) {

        }

        override fun onReturnEmvReportList(p0: Hashtable<String, String>?) {

        }

        override fun onReturnEnableInputAmountResult(p0: Boolean) {

        }

        override fun onAudioAutoConfigProgressUpdate(p0: Double) {

        }

        override fun onPowerButtonPressed() {

        }

        override fun onPrintDataCancelled() {

        }

        override fun onReturnNfcDetectCardResult(p0: BBDeviceController.NfcDetectCardResult?, p1: Hashtable<String, Any>?) {

        }

        override fun onPowerDown() {

        }

        override fun onReturnUpdateTerminalSettingResult(p0: BBDeviceController.TerminalSettingStatus?) {

        }

        override fun onAudioDevicePlugged() {

        }

        override fun onRequestKeypadResponse() {

        }

        override fun onNoAudioDeviceDetected() {

        }

        override fun onRequestSetAmount() {
            promptForAmount()
        }

        override fun onRequestDisplayLEDIndicator(p0: BBDeviceController.ContactlessStatus?) {

        }

        override fun onReturnFunctionKey(p0: BBDeviceController.FunctionKey?) {

        }

        override fun onReturnCAPKList(p0: MutableList<CAPK>?) {

        }

        override fun onRequestStartEmv() {

        }

        override fun onUsbDisconnected() {

        }

        override fun onReturnUpdateCAPKResult(p0: Boolean) {

        }

        override fun onRequestFinalConfirm() {
            bbDeviceController?.sendFinalConfirmResult(true)
        }

        override fun onReturnBarcode(p0: String?) {

        }

        override fun onReturnUpdateGprsSettingsResult(p0: Boolean, p1: Hashtable<String, BBDeviceController.TerminalSettingStatus>?) {

        }

        override fun onRequestPrintData(p0: Int, p1: Boolean) {

            onReceiptData.observe(currentFragment, androidx.lifecycle.Observer {
                bbDeviceController?.sendPrintData(it)
            })

        }

        override fun onSerialConnected() {
            if (currentFragment is ProcessTransactionFragment) startEmv()
        }

        override fun onReturnBatchData(p0: String?) {

        }

        override fun onReturnEncryptDataResult(p0: Boolean, p1: Hashtable<String, String>?) {


        }

        override fun onBarcodeReaderConnected() {

        }

        override fun onRequestDisplayAsterisk(numOfAsterisk: Int) {

            var content = ""
            if (currentFragment is PinPadFragment) {
            } else {
                content = "PIN" + ": "
            }
            for (i in 0 until numOfAsterisk) {
                content += "*"
            }
            (currentFragment as PinPadFragment).setStars(content)
        }

        override fun onReturnDeviceInfo(p0: Hashtable<String, String>?) {

        }

        override fun onReturnCancelCheckCardResult(p0: Boolean) {

        }

        override fun onBatteryLow(p0: BBDeviceController.BatteryStatus?) {

        }

        override fun onBTScanTimeout() {
            toolbarTitle.value = "Bluetooth scan timeout"
        }

        override fun onRequestProduceAudioTone(contactlessStatusTone: BBDeviceController.ContactlessStatusTone?) {
           val audio = activity?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val mode = audio.mode
            val currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * 3 / 4, 0)
            audio.mode = AudioManager.MODE_NORMAL
            if (contactlessStatusTone == BBDeviceController.ContactlessStatusTone.SUCCESS) {
                MediaPlayer.create(activity, R.raw.beep_ace_success).start()
            } else {
                MediaPlayer.create(activity, R.raw.beep_ace_alert).start()
            }

            Handler().postDelayed({
                // TODO Auto-generated method stub
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0)
                audio.mode = mode
            }, 500)
        }

        override fun onBTDisconnected() {

        }

        override fun onWaitingReprintOrPrintNext() {

        }

        override fun onSessionInitialized() {

        }

        override fun onReturnEnableAccountSelectionResult(p0: Boolean) {

        }

        override fun onWaitingForCard(cardMode: BBDeviceController.CheckCardMode?) {
            cancelTitle.value = "Cancel"
            when (cardMode) {
                BBDeviceController.CheckCardMode.INSERT -> {
                    toolbarTitle.value = "Please insert card"
                }
                BBDeviceController.CheckCardMode.SWIPE -> {
                    toolbarTitle.value = "Please swipe card"
                }
                BBDeviceController.CheckCardMode.SWIPE_OR_INSERT -> {
                    toolbarTitle.value = "Please swipe/insert card"
                }
                BBDeviceController.CheckCardMode.SWIPE_OR_TAP -> {
                    toolbarTitle.value = "Please swipe/tap card"
                }
                BBDeviceController.CheckCardMode.INSERT_OR_TAP -> {
                    toolbarTitle.value = "Please inert/tap card"
                }
                BBDeviceController.CheckCardMode.SWIPE_OR_INSERT_OR_TAP -> {
                    toolbarTitle.value = "Please swipe/insert/tap card"
                }
            }

        }

        override fun onReturnCheckCardResult(checkCardResult: CheckCardResult?, decodeData: Hashtable<String, String>) {

            if(checkCardResult == CheckCardResult.MSR) {
                paymentType  = PaymentType.CREDIT(TransactionPurchase.Action.SWIPE)

                var expiryDate = decodeData["expiryDate"].toString()

                cardData.ksn = decodeData["ksn"].toString()
                cardData.cardNumber = decodeData["pan"].toString()
                cardData.cardXNumber = decodeData["maskedPAN"].toString()
                cardData.expiryDate =  expiryDate
                cardData.expiryYear = expiryDate.substring(0..2)
                cardData.expiryMonth = expiryDate.substring(2..4)
                cardData.encTrack2 = decodeData["encTrack2"].toString()
                cardData.serviceCode = decodeData["serviceCode"].toString()
                cardData.posEntry = decodeData["posEntryMode"]!!.toInt()
                transaction.card = cardData
                proceedTransaction.value = true


            } else if(checkCardResult == CheckCardResult.INSERTED_CARD){

                paymentType  = PaymentType.CREDIT(TransactionPurchase.Action.EMV)
                val posEntryMode = decodeData["posEntryMode"]
                println(posEntryMode)
            } else if (checkCardResult == CheckCardResult.TAP_CARD_DETECTED){

            }

        }

        override fun onRequestPinEntry(pinEntrySource: BBDeviceController.PinEntrySource?) {

            if (pinEntrySource == BBDeviceController.PinEntrySource.SMARTPOS) {
                pinButtonLayout = Hashtable()

                pinButtonLayout["key1"] = Rect(50, 400, 255, 550)
                pinButtonLayout["key2"] = Rect(265, 400, 470, 550)
                pinButtonLayout["key3"] = Rect(480, 400, 685, 550)

                pinButtonLayout["key4"] = Rect(50, 560, 255, 710)
                pinButtonLayout["key5"] = Rect(265, 560, 470, 710)
                pinButtonLayout["key6"] = Rect(480, 560, 685, 710)

                pinButtonLayout["key7"] = Rect(50, 720, 255, 870)
                pinButtonLayout["key8"] = Rect(265, 720, 470, 870)
                pinButtonLayout["key9"] = Rect(480, 720, 685, 870)

                pinButtonLayout["cancel"] = Rect(50, 880, 255, 1030)
                pinButtonLayout["key0"] = Rect(265, 880, 470, 1030)
                pinButtonLayout["clear"] = Rect(480, 880, 685, 1030)

                pinButtonLayout["enter"] = Rect(50, 1040, 685, 1190)

                bbDeviceController!!.setPinPadButtons(pinButtonLayout)
            }

        }

        override fun onReturnSetPinPadButtonsResult(p0: Boolean) {
            val direction = ProcessTransactionFragmentDirections.actionProcessTransactionToPinPadFragment(amountStr)
            view!!.findNavController().navigate(direction)
        }

        override fun onReturnPinEntryResult(pinEntryResult: BBDeviceController.PinEntryResult?, data: Hashtable<String, String>) {
            cancelVisibility.value = View.INVISIBLE
            if (currentFragment is PinPadFragment){

                val direction = PinPadFragmentDirections.actionPinPadFragmentToProcessTransaction(amountStr)
                view!!.findNavController().navigate(direction)

            }

            if (pinEntryResult == BBDeviceController.PinEntryResult.ENTERED) run {
                if (data.containsKey("epb")) {
                   data.get("epb").toString()
                }
                if (data.containsKey("ksn")) {
                    data.get("ksn")
                }
                if (data.containsKey("randomNumber")) {
                    data.get("randomNumber")
                }
                if (data.containsKey("encWorkingKey")) {
                    data.get("encWorkingKey")
                }
            } else if (pinEntryResult == BBDeviceController.PinEntryResult.BYPASS) {

            } else if (pinEntryResult == BBDeviceController.PinEntryResult.CANCEL) {

            } else if (pinEntryResult == BBDeviceController.PinEntryResult.TIMEOUT) {

            }
            cancelVisibility.value = View.INVISIBLE

        }

        override fun onReturnTransactionResult(result: BBDeviceController.TransactionResult?) {

            if (result == BBDeviceController.TransactionResult.APPROVED){
                onTransactionResult.value = true
            }
            else {
                toolbarTitle.value = result.toString()
                cancelTitle.value = "Done"
                startAnimation.value = false
            }
            stopConnection()
        }
        override fun onDeviceReset() {

        }

        override fun onReturnReadWiFiSettingsResult(p0: Boolean, p1: Hashtable<String, Any>?) {

        }

        override fun onReturnDisplayPromptResult(p0: BBDeviceController.DisplayPromptResult?) {

        }

        override fun onBarcodeReaderDisconnected() {

        }

        override fun onUsbConnected() {

        }

        override fun onReturnUpdateWiFiSettingsResult(p0: Boolean, p1: Hashtable<String, BBDeviceController.TerminalSettingStatus>?) {

        }

        override fun onError(p0: BBDeviceController.Error?, errorStr: String?) {
            toolbarTitle.value = errorStr
            cancelTitle.value = "Done"
            startAnimation.value = false

        }

        override fun onReturnAmountConfirmResult(isSuccess: Boolean) {
            if (isSuccess) {
                toolbarTitle.value = "Amount Confirm"
            } else {
                toolbarTitle.value = "Amount Cancel"
            }

        }

        override fun onReturnInjectSessionKeyResult(p0: Boolean, p1: Hashtable<String, String>?) {

        }

        override fun onReturnPowerOffIccResult(p0: Boolean) {

        }
    }

}

