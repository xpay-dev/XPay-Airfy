package com.xpayworld.payment.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.gson.Gson
import com.xpayworld.payment.R
import com.xpayworld.payment.databinding.ActivityDashboardOfflineBinding
import com.xpayworld.payment.ui.base.kt.BaseActivity
import com.xpayworld.payment.ui.transaction.processTransaction.ARG_AMOUNT
import com.xpayworld.payment.ui.transaction.processTransaction.ARG_CURRENCY
import com.xpayworld.payment.util.IS_SDK
import com.xpayworld.payment.util.IS_TRANSACTION_OFFLINE
import com.xpayworld.payment.util.externalPackageName
import com.xpayworld.payment.util.transaction
import com.xpayworld.sdk.XPAY_REQUEST
import com.xpayworld.sdk.XpayRequest
import kotlinx.android.synthetic.main.activity_dashboard_offline.*
import kotlinx.android.synthetic.main.toolbar_main.*

class DashboardOfflineActivity : BaseActivity() {

    private lateinit var navController: NavController
    private lateinit var toolbar: Toolbar
    private val navBottomVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val toolBarVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val backButtonVisibility: MutableLiveData<Boolean> = MutableLiveData()
    val gson = Gson()

    var toolbarTitle: MutableLiveData<String> =  MutableLiveData()

    override fun initView() {


        val binding: ActivityDashboardOfflineBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_dashboard_offline)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        setupNavigation()
        supportActionBar?.title = ""


        val extras = intent.extras
        val xpayRequest = extras?.getString(XPAY_REQUEST)
        xpayRequest ?: return

        getRequest(request = xpayRequest.toString())

    }

    private fun setupNavigation() {
        navController = findNavController(R.id.mainNavigationFragment)
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            toolbar_title.text = destination.label
            when (destination.id) {
                R.id.payFragment, R.id.offlineFragment -> {
                    navBottomVisibility.value = true
                    backButtonVisibility.value = false
                }
                R.id.enterPinFragment, R.id.pinPadFragment ->{
                    toolBarVisibility.value = false
                    navBottomVisibility.value = false

                }
                R.id.preferenceFragment -> {
                    backButtonVisibility.value = true
                    navBottomVisibility.value = false
                }
                else -> {
                    backButtonVisibility.value = false
                    toolBarVisibility.value = true
                    navBottomVisibility.value = false
                }
            }
        }

        navBottomVisibility.observe(this, Observer {
            bottomNavigationView.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })

        toolBarVisibility.observe(this, Observer {
            if (it) supportActionBar?.show()
            else supportActionBar?.hide()
        })

        backButtonVisibility.observe(this, Observer {
            if (it) supportActionBar?.setDisplayHomeAsUpEnabled(true)
            else supportActionBar?.setDisplayHomeAsUpEnabled(false)
        })

        toolbarTitle.observe(this , Observer {
            toolbar_title.text = it
        })

        val appBarConfiguration = AppBarConfiguration.Builder(
                setOf(R.id.payFragment,
                        R.id.offlineFragment)).build()

        NavigationUI.setupWithNavController(bottomNavigationView, navController)

    }

    private fun getRequest(request: String){
        val data = gson.fromJson(request, XpayRequest::class.java)
        // initialization
        externalPackageName = data.appPackageName
        IS_TRANSACTION_OFFLINE = data.isOffine
        transaction.orderId = data.transactionId
        transaction.cardCaptureMethod = data.cardCaptureMethod
        transaction.currency = data.currency
        transaction.currencyCode = data.currencyCode
        IS_SDK = true
        val strAmount = "${data.amountPurchase}".replace(".", "")
        val graph =  navController.graph
        val b = Bundle()
        b.putString(ARG_AMOUNT,strAmount)
        b.putString(ARG_CURRENCY,data.currency)
        navController.setGraph(graph,b)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        shouldFullScreen()
        return super.dispatchTouchEvent(ev)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    fun View.setMarginTop(marginTop: Int) {
        val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
        menuLayoutParams.setMargins(0, marginTop, 0, 0)
        this.layoutParams = menuLayoutParams
    }
}