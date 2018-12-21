package com.sasaj.graphics.drawingapp.authentication.register

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.sasaj.domain.usecases.SignUp

class RegisterVMFactory(private val signUp: SignUp) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegisterViewModel(signUp) as T
    }
}
