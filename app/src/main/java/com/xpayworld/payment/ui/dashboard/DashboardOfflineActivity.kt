package com.xpayworld.payment.ui.dashboard

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.xpayworld.payment.R
import com.xpayworld.payment.databinding.ActivityDashboardOfflineBinding
import com.xpayworld.payment.ui.base.kt.BaseActivity
import kotlinx.android.synthetic.main.activity_dashboard_offline.*
import kotlinx.android.synthetic.main.toolbar_main.*

class DashboardOfflineActivity : BaseActivity(){

    private lateinit var navController: NavController
    private lateinit var toolbar: Toolbar
    private val navBottomVisibility : MutableLiveData<Boolean> = MutableLiveData()

    override fun initView() {
        val binding: ActivityDashboardOfflineBinding = DataBindingUtil.setContentView(this,
        R.layout.activity_dashboard_offline)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        setupNavigation()
    }

    private fun setupNavigation() {
        navController = findNavController( R.id.mainNavigationFragment)
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            when (destination.id) {
                R.id.payFragment, R.id.offlineFragment-> {
                    navBottomVisibility.value = true
                }
                else -> {
                    navBottomVisibility.value = false
                }
            }
        }

        navBottomVisibility.observe(this, Observer {
            bottomNavigationView.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })

        val appBarConfiguration = AppBarConfiguration.Builder(
                setOf(R.id.payFragment,
                        R.id.offlineFragment)).build()
        NavigationUI.setupActionBarWithNavController( this, navController,appBarConfiguration)
        NavigationUI.setupWithNavController (bottomNavigationView, navController)

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