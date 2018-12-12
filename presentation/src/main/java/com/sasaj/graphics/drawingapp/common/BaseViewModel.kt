package com.sasaj.graphics.drawingapp.common

import android.arch.lifecycle.ViewModel
import com.sasaj.graphics.drawingapp.DrawingApplication.Companion.injector
import com.sasaj.graphics.drawingapp.authentication.AuthenticationNavigationViewModel
import com.sasaj.graphics.drawingapp.authentication.viewmodels.ChangePasswordViewModel
import com.sasaj.graphics.drawingapp.authentication.viewmodels.LoginViewModel
import com.sasaj.graphics.drawingapp.authentication.viewmodels.RegisterViewModel
import com.sasaj.graphics.drawingapp.authentication.viewmodels.VerifyViewModel
import com.sasaj.graphics.drawingapp.drawing.DrawingViewModel
import com.sasaj.graphics.drawingapp.main.DrawingListViewModel
import com.sasaj.graphics.drawingapp.main.MainViewModel
import com.sasaj.graphics.drawingapp.splash.SplashViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

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
            is SplashViewModel -> injector.inject(this)
            is LoginViewModel -> injector.inject(this)
            is RegisterViewModel -> injector.inject(this)
            is VerifyViewModel -> injector.inject(this)
            is ChangePasswordViewModel -> injector.inject(this)
            is AuthenticationNavigationViewModel -> injector.inject(this)
            is MainViewModel -> injector.inject(this)
        }
    }


    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }
}