package com.xpayworld.payment.ui.base.kt

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.xpayworld.payment.R
import com.xpayworld.payment.ui.dashboard.UserInteraction
import com.xpayworld.payment.ui.dialog.ErrorDialog
import com.xpayworld.payment.ui.dialog.ErrorType
import com.xpayworld.payment.util.CustomDialog
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment : Fragment() , UserInteraction {

    private var parentActivity: BaseActivity? = null
    private lateinit var dialog: CustomDialog
    var mContainer: ViewGroup? = null
    var handler: Handler? = null
    var r: Runnable? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, mContainer)
        dialog = CustomDialog(context!!)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            val activity = context as BaseActivity?
            this.parentActivity = activity

            activity?.onFragmentAttached()
        }
    }

    fun hideProgress() {
        activity?.runOnUiThread {
            dialog.onDismiss()
        }
    }

    fun showProgress() {
            activity?.runOnUiThread {
                dialog.onLoading().show()
            }
    }

    fun showNetworkError(title: String? = null, message: String? = null, callBack: (() -> Unit)? = null) {
        showError(
                title?: "Network",
                message?: "Looks like we weren't able to connect to our server. Please check your connection and try again",
                 callBack)
    }

    fun showError(title: String , message: String, callBack: (() -> Unit)? = null){
        ErrorDialog().showAlert(
                title ,
                message,
                callBack,
                this)
    }
    override fun userInteractionListener() {

    }

    private fun performDependencyInjection() = AndroidSupportInjection.inject(this)

    fun getBaseActivity() = parentActivity

    interface CallBack {
        fun onFragmentAttached()
        fun onFragmentDetached(tag: String)
    }

    abstract fun initView(view: View, container: ViewGroup?)


}