package com.xpayworld.payment.ui.transaction.processTransaction

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import android.view.View
import java.io.Serializable

import java.util.Hashtable

internal class WisePOSPlusPinPadView(context: Context, var pinButton: Hashtable<String, Rect>) : View(context)  {
    var paint = Paint()
    var stars = ""

    private fun log(msg: String) {
        if (DEBUG_MODE) {
            Log.d(LOG_TAG, "[WisePOSPlusPinPadView] $msg")
        }
    }

    @SuppressLint("Range")
    public override fun onDraw(canvas: Canvas) {
        try {
            val cornerRadius = 20
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#FFFFFF")
            val wholeRect = RectF(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat())
            canvas.drawRect(wholeRect, paint)


            val keys = pinButton.keys.toTypedArray()
            for (key in keys) {
                val buttonRect = RectF(pinButton[key])
                log("[onDraw] buttonRect : $buttonRect")

                paint.strokeWidth = 1f
                paint.style = Paint.Style.FILL

                var buttonColor = ""
                var shadowColor = ""

                if ((key as String).equals("key0", ignoreCase = true)) {
                    buttonColor = "#E4E7E4"
                    shadowColor = "#CDCBC6"
                } else if (key.equals("key1", ignoreCase = true)) {
                    buttonColor = "#E4E7E4"
                    shadowColor = "#CDCBC6"
                } else if (key.equals("key2", ignoreCase = true)) {
                    buttonColor = "#E4E7E4"
                    shadowColor = "#CDCBC6"
                } else if (key.equals("key3", ignoreCase = true)) {
                    buttonColor = "#E4E7E4"
                    shadowColor = "#CDCBC6"
                } else if (key.equals("key4", ignoreCase = true)) {
                    buttonColor = "#E4E7E4"
                    shadowColor = "#CDCBC6"
                } else if (key.equals("key5", ignoreCase = true)) {
                    buttonColor = "#E4E7E4"
                    shadowColor = "#CDCBC6"
                } else if (key.equals("key6", ignoreCase = true)) {
                    buttonColor = "#E4E7E4"
                    shadowColor = "#CDCBC6"
                } else if (key.equals("key7", ignoreCase = true)) {
                    buttonColor = "#E4E7E4"
                    shadowColor = "#CDCBC6"
                } else if (key.equals("key8", ignoreCase = true)) {
                    buttonColor = "#E4E7E4"
                    shadowColor = "#CDCBC6"
                } else if (key.equals("key9", ignoreCase = true)) {
                    buttonColor = "#E4E7E4"
                    shadowColor = "#CDCBC6"
                } else if (key.equals("backspace", ignoreCase = true)) {
                    buttonColor = "#FFFFFFFF"
                    shadowColor = "#CDCBC6"
                } else if (key.equals("clear", ignoreCase = true)) {
                    buttonColor = "#FFC300"
                    shadowColor = "#D9AA0C"
                } else if (key.equals("cancel", ignoreCase = true)) {
                    buttonColor = "#FF2B2B"
                    shadowColor = "#C40D0D"
                } else if (key.equals("enter", ignoreCase = true)) {
                    buttonColor = "#50C40D"
                    shadowColor = "#3EA403"
                } else {
                    buttonColor = "#FFFFFFFF"
                    shadowColor = "#CDCBC6"
                }

                paint.color = Color.parseColor(shadowColor)
                paint.style = Paint.Style.FILL
                paint.strokeWidth = 0f
                val shadow = RectF(pinButton[key]!!.left.toFloat(), pinButton[key]!!.top.toFloat(), pinButton[key]!!.right.toFloat(), pinButton[key]!!.bottom + 8f)
                canvas.drawRoundRect(shadow, cornerRadius.toFloat(), cornerRadius.toFloat(), paint)

                paint.color = Color.parseColor(buttonColor)
                canvas.drawRoundRect(buttonRect, cornerRadius.toFloat(), cornerRadius.toFloat(), paint)

                setTextSizeForWidthAndHeight(paint, (buttonRect.width() * 0.5).toFloat(), (buttonRect.height() * 0.5).toFloat(), "2")
                if (key.equals("key0", ignoreCase = true)) {
                    drawText1(canvas, buttonRect, paint, "0", true)
                } else if (key.equals("key1", ignoreCase = true)) {
                    drawTwoLineText(canvas, buttonRect, paint, "1/QZ")
                } else if (key.equals("key2", ignoreCase = true)) {
                    drawTwoLineText(canvas, buttonRect, paint, "2/ABC")
                } else if (key.equals("key3", ignoreCase = true)) {
                    drawTwoLineText(canvas, buttonRect, paint, "3/DEF")
                } else if (key.equals("key4", ignoreCase = true)) {
                    drawTwoLineText(canvas, buttonRect, paint, "4/GHI")
                } else if (key.equals("key5", ignoreCase = true)) {
                    drawTwoLineText(canvas, buttonRect, paint, "5/JKL")
                } else if (key.equals("key6", ignoreCase = true)) {
                    drawTwoLineText(canvas, buttonRect, paint, "6/MNO")
                } else if (key.equals("key7", ignoreCase = true)) {
                    drawTwoLineText(canvas, buttonRect, paint, "7/PRS")
                } else if (key.equals("key8", ignoreCase = true)) {
                    drawTwoLineText(canvas, buttonRect, paint, "8/TUV")
                } else if (key.equals("key9", ignoreCase = true)) {
                    drawTwoLineText(canvas, buttonRect, paint, "9/WXY")
                } else if (key.equals("enter", ignoreCase = true)) {
                    //drawText1(canvas, buttonRect, paint,"⏎", true);
                    drawText1(canvas, buttonRect, paint, "↵", true)
                } else if (key.equals("backspace", ignoreCase = true)) {
                    drawText1(canvas, buttonRect, paint, "⇦", true)
                } else if (key.equals("clear", ignoreCase = true)) {
                    drawText1(canvas, buttonRect, paint, "↼", true)
                } else if (key.equals("cancel", ignoreCase = true)) {
                    drawText1(canvas, buttonRect, paint, "x", true)
                }
            }
            paint.textSize = 40f
            val pin = RectF(0f, (canvas.height * 0.1).toFloat(), canvas.width.toFloat(), canvas.height * 0.1f)
            drawText1(canvas, pin, paint, "Enter PIN code", true)
            paint.color = Color.GRAY
            val starsRect = RectF(0f, (canvas.height * 0.15).toFloat(), canvas.width.toFloat(), canvas.height * 0.2f)
            paint.textSize = 100f
            drawTextWithLine(canvas, starsRect, paint, stars, true)
        } catch (e: Exception) {
            log("[onDraw] e : $e")
        }

    }

    private fun drawTwoLineText(canvas: Canvas, rF: RectF, paint: Paint, text: String) {
        val r = Rect()
        paint.style = Paint.Style.FILL

        if (text.equals("↵", ignoreCase = true) || text.equals("⇦", ignoreCase = true) ||
                text.equals("↼", ignoreCase = true) || text.equals("X", ignoreCase = true)) {
            paint.color = Color.WHITE
        } else {
            paint.color = Color.BLACK
        }

        canvas.getClipBounds(r)

        val separated = text.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val cHeight = rF.height()
        val cWidth = rF.width()
        paint.textAlign = Paint.Align.LEFT
        paint.getTextBounds(text, 0, separated[0].length, r)
        val x = cWidth / 2f - r.width() / 2f - r.left.toFloat() + rF.left
        val y = cHeight / 2f + r.height() / 2f - r.bottom + rF.top

        setTextSizeForWidthAndHeight(paint, (rF.width() * 0.25).toFloat(), (rF.height() * 0.25).toFloat(), separated[0])
        canvas.drawText(separated[0], x, y - setTextSizeForWidthAndHeightReturnFloat(paint, (rF.width() * 0.25).toFloat(), (rF.height() * 0.25).toFloat(), separated[0]) / 2, paint)

        paint.getTextBounds(text, 0, separated[1].length, r)
        setTextSizeForWidthAndHeight(paint, (rF.width() * 0.21).toFloat(), (rF.height() * 0.21).toFloat(), separated[1])
        canvas.drawText(separated[1], x, y, paint)
    }

    private fun drawText1(canvas: Canvas, rF: RectF, paint: Paint, text: String, isIncludeDescent: Boolean) {
        paint.textAlign = Paint.Align.LEFT
        paint.style = Paint.Style.FILL
        val r = Rect()

        if (text.equals("↵", ignoreCase = true) || text.equals("⇦", ignoreCase = true) ||
                text.equals("↼", ignoreCase = true) || text.equals("X", ignoreCase = true)) {
            paint.color = Color.WHITE
        } else {
            paint.color = Color.BLACK
        }

        val cHeight = rF.height()
        val cWidth = rF.width()
        paint.getTextBounds(text, 0, text.length, r)
        val x = cWidth / 2 - r.width() / 2f - r.left.toFloat() + rF.left


        if (isIncludeDescent) {
            val y = cHeight / 2 + r.height() / 2f - r.bottom + rF.top
            canvas.drawText(text, x, y, paint)
        } else {
            val y = cHeight / 2 + r.height() / 2f + rF.top
            canvas.drawText(text, x, y, paint)
        }
    }

    private fun drawTextWithLine(canvas: Canvas, rF: RectF, paint: Paint, text: String, isIncludeDescent: Boolean) {
        paint.textAlign = Paint.Align.LEFT
        paint.style = Paint.Style.FILL
        val r = Rect()

        if (text.equals("↵", ignoreCase = true) || text.equals("⇦", ignoreCase = true) ||
                text.equals("↼", ignoreCase = true) || text.equals("X", ignoreCase = true)) {
            paint.color = Color.WHITE
        } else {
            paint.color = Color.BLACK
        }

        val cHeight = rF.height()
        val cWidth = rF.width()
        paint.getTextBounds(text, 0, text.length, r)
        val x = cWidth / 2 - r.width() / 2f - r.left.toFloat() + rF.left


        if (isIncludeDescent) {
            val y = cHeight / 2 + r.height() / 2f - r.bottom + rF.top
            canvas.drawText(text, x, y, paint)
            paint.color = Color.parseColor("#06A517")
            paint.strokeWidth = 2f
            canvas.drawLine(x, (canvas.height * 0.22).toFloat(), x + r.width(), (canvas.height * 0.22).toFloat(), paint)
        } else {
            val y = cHeight / 2 + r.height() / 2f + rF.top
            canvas.drawText(text, x, y, paint)
            paint.color = Color.parseColor("#06A517")
            paint.strokeWidth = 2f
            canvas.drawLine(x, (canvas.height * 0.22).toFloat(), x + r.width(), (canvas.height * 0.22).toFloat(), paint)
        }
    }


    companion object {

        private val DEBUG_MODE = false

        private val LOG_TAG = WisePOSPlusPinPadView::class.java.name

        fun setTextSizeForWidthAndHeight(paint: Paint, desiredWidth: Float, desiredHeight: Float, text: String): RectF {
            val testTextSize = 48f

            // Get the bounds of the text, using our testTextSize.
            paint.textSize = testTextSize
            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)

            // Calculate the desired size as a proportion of our testTextSize.
            val desiredTextSize1 = testTextSize * desiredWidth / bounds.width()
            val desiredTextSize2 = testTextSize * desiredHeight / bounds.height()

            // Set the paint for that size.
            if (desiredTextSize1 < desiredTextSize2) {
                paint.textSize = desiredTextSize1
            } else {
                paint.textSize = desiredTextSize2
            }
            paint.getTextBounds(text, 0, text.length, bounds)
            return RectF(bounds)
        }

        fun setTextSizeForWidthAndHeightReturnFloat(paint: Paint, desiredWidth: Float, desiredHeight: Float, text: String): Float {
            val testTextSize = 48f

            // Get the bounds of the text, using our testTextSize.
            paint.textSize = testTextSize
            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)

            // Calculate the desired size as a proportion of our testTextSize.
            val desiredTextSize1 = testTextSize * desiredWidth / bounds.width()
            val desiredTextSize2 = testTextSize * desiredHeight / bounds.height()

            // Set the paint for that size.
            if (desiredTextSize1 < desiredTextSize2) {
                paint.textSize = desiredTextSize1
            } else {
                paint.textSize = desiredTextSize2
            }
            paint.getTextBounds(text, 0, text.length, bounds)

            return if (desiredTextSize1 < desiredTextSize2) {
                desiredTextSize1
            } else {
                desiredTextSize2
            }
        }

        fun setStars(wisePOSPlusPinPadView: WisePOSPlusPinPadView, stars: String) {
            wisePOSPlusPinPadView.stars = stars
        }
    }
}
