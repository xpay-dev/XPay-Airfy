package com.xpayworld.payment.ui.dialog

import android.app.Dialog
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.xpayworld.payment.R
import com.xpayworld.payment.ui.base.kt.BaseDialogFragment

class LoadingDialog : DialogFragment() {

    fun show(targetFragment: Fragment) {
        val dialog = LoadingDialog()
        dialog.setTargetFragment(targetFragment, 0)
        targetFragment.fragmentManager?.let { dialog.show(it, "tag") }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.view_loading)
        val img = dialog.findViewById<ImageView>(R.id.imgLoading)
        val anim = img?.drawable as AnimationDrawable
        anim.start()
        return dialog
    }
}