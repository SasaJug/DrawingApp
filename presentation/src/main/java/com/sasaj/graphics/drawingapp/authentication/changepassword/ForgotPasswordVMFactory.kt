package com.sasaj.graphics.drawingapp.authentication.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sasaj.domain.usecases.ChangePasswordUseCase
import com.sasaj.domain.usecases.NewPassword

class ForgotPasswordVMFactory(private val changePasswordUseCase: ChangePasswordUseCase, private val newPasswordUseCase: NewPassword) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForgotPasswordViewModel(changePasswordUseCase, newPasswordUseCase) as T
    }
}