package com.sasaj.graphics.drawingapp.authentication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sasaj.domain.usecases.VerifyUser

class VerifyVMFactory(private val verifyUser: VerifyUser) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VerifyViewModel(verifyUser) as T
    }
}