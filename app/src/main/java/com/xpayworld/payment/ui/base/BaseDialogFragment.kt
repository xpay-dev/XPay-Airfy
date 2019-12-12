package com.xpayworld.payment.ui.base.kt

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.xpayworld.payment.R


abstract class BaseDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        dialog.setCancelable(false)

        val displayMetrics = DisplayMetrics()
        activity!!.windowManager!!.defaultDisplay?.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels


        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.width =  (width - (width * 0.3)).toInt()
    }
}