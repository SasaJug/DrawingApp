package com.sasaj.graphics.drawingapp.splash

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.sasaj.domain.usecases.CheckIfLoggedIn

class SplashVMFactory(private val checkIfLoggedIn: CheckIfLoggedIn) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(checkIfLoggedIn) as T
    }
}