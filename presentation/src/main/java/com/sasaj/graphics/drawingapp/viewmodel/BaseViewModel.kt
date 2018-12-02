package com.sasaj.graphics.drawingapp.viewmodel

import android.arch.lifecycle.ViewModel
import com.sasaj.graphics.drawingapp.DrawingApplication.Companion.injector

abstract class BaseViewModel : ViewModel() {

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is DrawingListViewModel -> injector.inject(this)
            is DrawingViewModel -> injector.inject(this)
            is SelectBrushViewModel -> injector.inject(this)
            is SplashViewModel -> injector.inject(this)
            is LoginViewModel -> injector.inject(this)
            is RegisterViewModel -> injector.inject(this)
            is VerifyViewModel -> injector.inject(this)
        }
    }
}