package com.xpayworld.payment.di.module

import android.app.Application
import android.content.Context
import com.xpayworld.payment.di.ApplicationContext
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val mApplication: Application) {

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context {
        return mApplication
    }

    @Provides
    internal fun provideApplication(): Application {
        return mApplication
    }
}
