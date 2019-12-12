package com.xpayworld.payment.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.xpayworld.payment.di.ActivityContext
import dagger.Module
import dagger.Provides


@Module
class ActivityModule(private val mActivity: AppCompatActivity) {

    @Provides
    @ActivityContext
    internal fun provideContext(): Context {
        return mActivity
    }

    @Provides
    internal fun provideActivity(): AppCompatActivity {
        return mActivity
    }
}
