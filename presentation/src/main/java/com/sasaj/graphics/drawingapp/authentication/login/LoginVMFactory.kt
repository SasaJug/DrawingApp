package com.sasaj.graphics.drawingapp.authentication.login

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.sasaj.domain.usecases.LogIn

class LoginVMFactory(private val logInUseCase: LogIn) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(logInUseCase) as T
    }

}
