package com.sasaj.graphics.drawingapp.authentication.changepassword

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sasaj.domain.usecases.ChangePasswordUseCase
import com.sasaj.domain.usecases.NewPassword
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import com.sasaj.graphics.drawingapp.common.UIException


class ForgotPasswordViewModel(private val changePasswordUseCase: ChangePasswordUseCase, private val newPasswordUseCase: NewPassword) : BaseViewModel() {

    val forgotPasswordLiveData: MutableLiveData<ForgotPasswordViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<UIException> = SingleLiveEvent()

    init {
        forgotPasswordLiveData.value = ForgotPasswordViewState()
    }

    fun changePassword(username: String) {
        addDisposable(changePasswordUseCase.changePassword(username)
                .subscribe(
                        { b: Boolean ->
                            if (b) {
                                val newPasswordChangeViewState = forgotPasswordLiveData.value?.copy(passwordChangeStarted = true,
                                        loading = false,
                                        isPasswordChangeRequested = true,
                                        isPasswordChanged = false)
                                forgotPasswordLiveData.value = newPasswordChangeViewState
                                errorState.value = null
                            }
                        },
                        { e -> errorState.value = UIException(cause = e) },
                        { Log.i(TAG, "Password change requested") }
                ))
    }

    fun newPassword(code: String, password: String, confirmPassword: String) {
        var errorCode = 0

        if (code.trim() == "")
            errorCode = errorCode or UIException.EMPTY_CODE
        if (password.trim() == "")
            errorCode = errorCode or UIException.EMPTY_PASSWORD
        if (confirmPassword.trim() == "")
            errorCode = errorCode or UIException.EMPTY_CONFIRM_PASSWORD
        if (password != confirmPassword)
            errorCode = errorCode or UIException.PASSWORDS_DO_NOT_MATCH

        if (errorCode > 0) {
            errorState.value = UIException("All entries must be valid", IllegalArgumentException(), errorCode)
            return
        }

        val passwordChangeViewState = forgotPasswordLiveData.value?.copy(passwordChangeStarted = true,
                loading = true,
                isPasswordChangeRequested = true,
                isPasswordChanged = false)
        forgotPasswordLiveData.value = passwordChangeViewState
        addDisposable(newPasswordUseCase.newPassword(password, code)
                .subscribe(
                        { b: Boolean ->
                            if (b) {
                                val newPasswordChangeViewState = forgotPasswordLiveData.value?.copy(passwordChangeStarted = true,
                                        loading = false,
                                        isPasswordChangeRequested = false,
                                        isPasswordChanged = true)
                                forgotPasswordLiveData.value = newPasswordChangeViewState
                                errorState.value = null
                            }
                        },
                        { e -> errorState.value = UIException(cause = e) },
                        { Log.e(TAG, "Password changed successfully") }
                ))
    }

    companion object {

        private val TAG = ForgotPasswordViewModel::class.java.simpleName
    }

}