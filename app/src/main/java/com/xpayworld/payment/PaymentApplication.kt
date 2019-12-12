package com.xpayworld.payment

import android.app.Application
import com.xpayworld.payment.di.component.ApplicationComponent
import com.xpayworld.payment.di.component.DaggerApplicationComponent
import com.xpayworld.payment.di.module.ApplicationModule


class PaymentApplication : Application() {

    var appComponent: ApplicationComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
        appComponent!!.inject(this)

    }
}
