package com.xpayworld.payment.ui.dialog

import androidx.fragment.app.DialogFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.app.AlertDialog
import com.xpayworld.payment.R
import android.app.Dialog
import android.app.UiAutomation
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.xpayworld.payment.ui.base.kt.BaseDialogFragment
import android.view.WindowManager

enum class ErrorType {
    NETWORK,REQUEST
}

class ErrorDialog : BaseDialogFragment() {

    private val ARG_TITLE = "AlertDialog.Title"
    private val ARG_MESSAGE = "AlertDialog.Message"
    private var callback : (()-> Unit)? = null


    fun showAlert(title: String, message: String,callback : (()-> Unit)? = null,  targetFragment: Fragment) {
        val dialog = ErrorDialog()
        dialog.isCancelable = false


        val args = Bundle()
        dialog.callback = callback
        args.putString(ARG_TITLE, title)
        args.putString(ARG_MESSAGE, message)
        dialog.arguments = args
        dialog.setTargetFragment(targetFragment, 0)
        targetFragment.fragmentManager?.let {
            dialog.show(it, "tag")
            dialog.dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_error, null)
        val alertDialog = AlertDialog.Builder(activity)
                .setView(mDialogView)
                .create()

        // Temporarily set the dialogs window to not focusable to prevent the short
        // popup of the navigation bar.

        val btnDone = mDialogView.findViewById<Button>(R.id.btnDone)
        val title = mDialogView.findViewById<TextView>(R.id.tvTitle)
        val message =  mDialogView.findViewById<TextView>(R.id.tvMessage)

        title.text = arguments?.get(ARG_TITLE).toString()
        message.text = arguments?.get(ARG_MESSAGE).toString()

        btnDone.setOnClickListener {
            callback?.invoke()
            alertDialog.dismiss()
        }

        return  alertDialog
    }
}