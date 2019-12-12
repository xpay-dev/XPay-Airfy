package com.xpayworld.payment.di.component

import com.xpayworld.payment.di.PerActivity
import com.xpayworld.payment.di.module.ActivityModule

import dagger.Component

@PerActivity
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent//    void inject(ActivationActivity activity);
