package com.sasaj.graphics.drawingapp.authentication.register

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.sasaj.domain.usecases.VerifyUser

class VerifyVMFactory(private val verifyUser: VerifyUser) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VerifyViewModel(verifyUser) as T
    }
}