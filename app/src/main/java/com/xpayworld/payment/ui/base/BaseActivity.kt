package com.xpayworld.payment.ui.base.kt

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.xpayworld.payment.R
import com.xpayworld.payment.ui.activation.ActivationFragment
import com.xpayworld.payment.ui.dashboard.UserInteraction
import com.xpayworld.payment.ui.dialog.ErrorDialog
import com.xpayworld.payment.ui.enterPin.EnterPinFragment
import com.xpayworld.payment.util.CustomDialog
import dagger.android.AndroidInjection


abstract  class BaseActivity : AppCompatActivity() ,BaseFragment.CallBack{
    private lateinit var dialog: CustomDialog
    var handler: Handler? = null
    var r: Runnable? = null
    var userInteraction: UserInteraction? = null
    internal val PERMISSION_REQUEST_CODE = 200

    lateinit var navHostFragment : Fragment
    lateinit var currentFragment : Fragment
    public val UserInteraction :  MutableLiveData<String> =  MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = CustomDialog(applicationContext)
        initView()
        requestPermission()

        handler = Handler()
        r = Runnable {

            navHostFragment =  supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
            currentFragment =  navHostFragment.childFragmentManager.fragments[0]

            userInteraction?.userInteractionListener()

            if ((currentFragment !is ActivationFragment && currentFragment !is EnterPinFragment) ){
                ErrorDialog().showAlert(
                        "Session Time out",
                        "Sorry , your session timed out after a long time of inactivity, Please Log in again",
                        {
                            findNavController(R.id.nav_host_fragment).navigate(R.id.logoutFragment)
                        },
                        currentFragment)
            }

        }
        startHandler()
    }

    fun showProgress() {
        runOnUiThread {
            dialog.onLoading().show()}
    }

    fun hideProgress() {
        runOnUiThread {
            dialog.onDismiss()}
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    abstract fun initView()

    private fun performDI() = AndroidInjection.inject(this)


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        shouldSetToFullScreen()
    }

    //@Override
    @SuppressLint("Override")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted.
                } else {
                    // permission denied.
                }
                return
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        UserInteraction.value = ""
        stopHandler()
        startHandler()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopHandler()
    }



    fun stopHandler() {
        handler?.removeCallbacks(r)
    }

    fun startHandler() {
        handler?.postDelayed(r, (5* 60 * 1000).toLong()) //for 5 minutes
    }

    fun shouldSetToFullScreen(){
        if (Build.VERSION_CODES.KITKAT <= VERSION.SDK_INT) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }

    fun requestPermission(){
        // Check permission
        if (VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                val permissionList = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE)
                ActivityCompat.requestPermissions(this, permissionList, PERMISSION_REQUEST_CODE)
            }
        }
    }
}