package com.xpayworld.payment.ui.transaction.receipt

import android.content.Context
import android.graphics.*
import com.bbpos.bbdevice.BBDeviceController
import com.xpayworld.payment.R
import com.xpayworld.payment.network.PosWsResponse
import com.xpayworld.payment.network.TransactionResponse
import com.xpayworld.payment.util.BaseViewModel
import com.xpayworld.payment.util.merchantDetails
import java.io.ByteArrayOutputStream

class ReceiptViewModel(context: Context) : BaseViewModel(){
    private val INIT = byteArrayOf(0x1B, 0x40)
    private val POWER_ON = byteArrayOf(0x1B, 0x3D, 0x01)
    private val POWER_OFF = byteArrayOf(0x1B, 0x3D, 0x02)
    private val NEW_LINE = byteArrayOf(0x0A)
    private val ALIGN_LEFT = byteArrayOf(0x1B, 0x61, 0x00)
    private val ALIGN_CENTER = byteArrayOf(0x1B, 0x61, 0x01)
    private val ALIGN_RIGHT = byteArrayOf(0x1B, 0x61, 0x02)
    private val EMPHASIZE_ON = byteArrayOf(0x1B, 0x45, 0x01)
    private val EMPHASIZE_OFF = byteArrayOf(0x1B, 0x45, 0x00)
    private val FONT_5X8 = byteArrayOf(0x1B, 0x4D, 0x00)
    private val FONT_5X12 = byteArrayOf(0x1B, 0x4D, 0x01)
    private val FONT_8X12 = byteArrayOf(0x1B, 0x4D, 0x02)
    private val FONT_10X18 = byteArrayOf(0x1B, 0x4D, 0x03)
    private val FONT_SIZE_0 = byteArrayOf(0x1D, 0x21, 0x00)
    private val FONT_SIZE_1 = byteArrayOf(0x1D, 0x21, 0x11)
    private val CHAR_SPACING_0 = byteArrayOf(0x1B, 0x20, 0x00)
    private val CHAR_SPACING_1 = byteArrayOf(0x1B, 0x20, 0x01)
    private val KANJI_FONT_24X24 = byteArrayOf(0x1C, 0x28, 0x41, 0x02, 0x00, 0x30, 0x00)
    private val KANJI_FONT_16X16 = byteArrayOf(0x1C, 0x28, 0x41, 0x02, 0x00, 0x30, 0x01)

    fun genReceipt(context: Context): ByteArray {

        val lineWidth = 384
        val size0NoEmphasizeLineWidth = 384 / 8 //line width / font width

        var singleLine = ""
        for (i in 0 until size0NoEmphasizeLineWidth) {
            singleLine += "-"
        }
        var doubleLine = ""
        for (i in 0 until size0NoEmphasizeLineWidth) {
            doubleLine += "="
        }
        val baos = ByteArrayOutputStream()

        baos.write(INIT)
        baos.write(POWER_ON)
        baos.write(NEW_LINE)
        baos.write(ALIGN_CENTER)

        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.xpay)
        val targetWidth = 384
        val targetHeight = Math.round(targetWidth.toDouble() / bitmap.width.toDouble() * bitmap.height.toDouble()).toInt()
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)
        val imageCommand = BBDeviceController.getImageCommand(scaledBitmap, 150)

        baos.write(imageCommand, 0, imageCommand.size)

        baos.write(NEW_LINE)
        baos.write(CHAR_SPACING_0)
        baos.write(FONT_SIZE_0)
        baos.write(EMPHASIZE_ON)
        baos.write(FONT_10X18)
        val address = merchantDetails?.address+ ","+ merchantDetails?.city +","+ merchantDetails?.country

        baos.write("${merchantDetails?.branchName}".toByteArray());
        baos.write(NEW_LINE);
        baos.write(FONT_5X12)
        baos.write("${address}".toByteArray());
        baos.write(NEW_LINE);

        baos.write(FONT_SIZE_1);
        baos.write(KANJI_FONT_16X16);
        baos.write("${merchantDetails?.merchantName}".toByteArray());
        baos.write(FONT_5X12);
        baos.write(NEW_LINE);

        baos.write(FONT_8X12);
        baos.write(singleLine.toByteArray());
        baos.write(NEW_LINE);

        baos.write(ALIGN_LEFT);


        return baos.toByteArray()
    }

    fun getReceiptSerial(context: Context , txn : TransactionResponse , posStatus : PosWsResponse): ByteArray? {
        try {
            val baos = ByteArrayOutputStream()


            val bitmap = Bitmap.createBitmap(384, 1300, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawARGB(255, 255, 255, 255)


            var x = 0
            var y = 0
            val paintText = Paint()
            paintText.color = Color.parseColor("#FF000000")
            paintText.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            paintText.textSize = 86f
            paintText.isAntiAlias = true
            x = 50
            y = 100
            canvas.drawText("XPAY", x.toFloat(), y.toFloat(), paintText)

            paintText.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paintText.textSize = 16f
            x = 80
            y += 30
            val address = merchantDetails?.address+ ","+ merchantDetails?.city +","+ merchantDetails?.country

            canvas.drawText("${merchantDetails?.branchName}", x.toFloat(), y.toFloat(), paintText)
            y += 20
            canvas.drawText(address, x.toFloat(), y.toFloat(), paintText)


            x = 60
            y += 50
            paintText.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
            paintText.textSize = 26f
            canvas.drawText("OFFICIAL RECEIPT", x.toFloat(), y.toFloat(), paintText)

            x = 0
            y += 30
            paintText.textSize = 16f
            canvas.drawText("_____________________________________________", x.toFloat(), y.toFloat(), paintText)
            x = 20
            y += 30
            canvas.drawText("TRX NO", x.toFloat(), y.toFloat(), paintText)
            x = 130
            paintText.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
            canvas.drawText("${txn.transNumber}", x.toFloat(), y.toFloat(), paintText)
            x = 20
            y += 20
            paintText.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
            canvas.drawText("DATE/TIME", x.toFloat(), y.toFloat(), paintText)
            x = 130
            paintText.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
            canvas.drawText("${txn.timestamp}", x.toFloat(), y.toFloat(), paintText)
            x = 0
            y += 20
            paintText.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
            canvas.drawText("_____________________________________________", x.toFloat(), y.toFloat(), paintText)


            x = 20
            y += 40
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
            canvas.drawText("Merchant Id : ", x.toFloat(), y.toFloat(), paintText)
            x = 280
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            canvas.drawText("${txn.merchantId}", x.toFloat(), y.toFloat(), paintText)
            x = 20
            y += 20
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
            canvas.drawText("Terminal Id :", x.toFloat(), y.toFloat(), paintText)
            x = 280
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            canvas.drawText("${txn.terminalId}", x.toFloat(), y.toFloat(), paintText)
            x = 20
            y += 20
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
            canvas.drawText("Batch Number :", x.toFloat(), y.toFloat(), paintText)
            x = 280
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            canvas.drawText("${txn.batchNumber}", x.toFloat(), y.toFloat(), paintText)

            x = 20
            y += 20
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
            canvas.drawText("Invoice Number :", x.toFloat(), y.toFloat(), paintText)
            x = 280
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            canvas.drawText("${txn.transNumber}", x.toFloat(), y.toFloat(), paintText)

            x = 20
            y += 60
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
            canvas.drawText("Card Number :", x.toFloat(), y.toFloat(), paintText)
            x = 280
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            canvas.drawText("${txn.cardNumber}", x.toFloat(), y.toFloat(), paintText)
            x = 20
            y += 20
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
            canvas.drawText("Card Type :", x.toFloat(), y.toFloat(), paintText)
            x = 280
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            canvas.drawText("${txn.cardType}", x.toFloat(), y.toFloat(), paintText)
            x = 20
            y += 20
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
            canvas.drawText("Auth Number :", x.toFloat(), y.toFloat(), paintText)
            x = 280
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            canvas.drawText("${txn.authNumber}", x.toFloat(), y.toFloat(), paintText)

            x = 20
            y += 20
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
            canvas.drawText("Transaction Status :", x.toFloat(), y.toFloat(), paintText)
            x = 280
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            canvas.drawText("${posStatus.status}", x.toFloat(), y.toFloat(), paintText)

            x = 20
            y += 60
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
            canvas.drawText("Amount :", x.toFloat(), y.toFloat(), paintText)
            x = 280
            paintText.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            canvas.drawText(txn.currency + ' ' + txn.total, x.toFloat(), y.toFloat(), paintText)


            val imageCommand = BBDeviceController.getImageCommand(bitmap, 150)
            baos.write(imageCommand, 0, imageCommand.size)

            return baos.toByteArray()

        } catch ( e : Exception) {
            e.printStackTrace()
        }

        return null
    }

}