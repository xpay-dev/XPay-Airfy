package com.xpayworld.payment.util

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.xpayworld.payment.R
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.dialog_error.*


class CustomDialog(context: Context) : DialogUI {

    var mDialog: Dialog? = null

    init {
        mDialog = Dialog(context)
        mDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val v = mDialog?.window!!.decorView
        v.setBackgroundResource(android.R.color.transparent)
    }


    override fun onDismiss() {
        mDialog?.dismiss()
    }

    override fun onLoading(): Dialog {
        mDialog!!.setContentView(R.layout.view_loading)
        val img = mDialog!!.findViewById<ImageView>(R.id.imgLoading)
        val anim = img?.drawable as AnimationDrawable
        anim.start()
        mDialog!!.setCancelable(false)
        mDialog!!.setCanceledOnTouchOutside(false)
        return mDialog!!
    }
}