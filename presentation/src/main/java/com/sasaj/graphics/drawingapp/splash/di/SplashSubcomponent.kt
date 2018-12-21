package com.sasaj.graphics.drawingapp.splash.di

import com.sasaj.graphics.drawingapp.splash.SplashActivity
import dagger.Subcomponent

@SplashScope
@Subcomponent(modules = [SplashModule::class])
interface SplashSubcomponent {
    fun inject(splashActivity : SplashActivity)
}