package com.sasaj.graphics.drawingapp.authentication.changepassword

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sasaj.domain.usecases.ChangePasswordUseCase
import com.sasaj.domain.usecases.NewPassword
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent


class ForgotPasswordViewModel(private val changePasswordUseCase: ChangePasswordUseCase, private val newPasswordUseCase: NewPassword) : BaseViewModel() {


    val forgotPasswordLiveData: MutableLiveData<ForgotPasswordViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

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
                        { e -> errorState.value = e },
                        { Log.i(TAG, "Password change requested") }
                ))
    }


    fun newPassword(password: String, code: String) {
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
                        { e -> errorState.value = e },
                        { Log.e(TAG, "Password changed successfully") }
                ))
    }

    companion object {

        private val TAG = ForgotPasswordViewModel::class.java.simpleName
    }

}