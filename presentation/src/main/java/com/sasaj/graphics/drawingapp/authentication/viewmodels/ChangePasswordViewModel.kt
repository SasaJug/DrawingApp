package com.sasaj.graphics.drawingapp.authentication.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sasaj.domain.usecases.ChangePassword
import com.sasaj.domain.usecases.NewPassword
import com.sasaj.graphics.drawingapp.authentication.states.PasswordChangeViewState
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import javax.inject.Inject


class ChangePasswordViewModel : BaseViewModel() {

    @Inject
    lateinit var changePasswordUseCase: ChangePassword

    @Inject
    lateinit var newPasswordUseCase: NewPassword

    val passwordChangeLiveData: MutableLiveData<PasswordChangeViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    init {
        passwordChangeLiveData.value = PasswordChangeViewState()
    }

    fun changePassword(username: String) {
        addDisposable(changePasswordUseCase.changePassword(username)
                .subscribe(
                        { b: Boolean ->
                            if (b) {
                                val newPasswordChangeViewState = passwordChangeLiveData.value?.copy(passwordChangeStarted = true,
                                        loading = false,
                                        isPasswordChangeRequested = true,
                                        isPasswordChanged = false)
                                passwordChangeLiveData.value = newPasswordChangeViewState
                                errorState.value = null
                            }
                        },
                        { e -> errorState.value = e },
                        { Log.i(TAG, "Password change requested") }
                ))
    }


    fun newPassword(password: String, code: String) {
        val passwordChangeViewState = passwordChangeLiveData.value?.copy(passwordChangeStarted = true,
                loading = true,
                isPasswordChangeRequested = true,
                isPasswordChanged = false)
        passwordChangeLiveData.value = passwordChangeViewState
        addDisposable(newPasswordUseCase.newPassword(password, code)
                .subscribe(
                        { b: Boolean ->
                            if (b) {
                                val newPasswordChangeViewState = passwordChangeLiveData.value?.copy(passwordChangeStarted = true,
                                        loading = false,
                                        isPasswordChangeRequested = false,
                                        isPasswordChanged = true)
                                passwordChangeLiveData.value = newPasswordChangeViewState
                                errorState.value = null
                            }
                        },
                        { e -> errorState.value = e },
                        { Log.e(TAG, "Password changed successfully") }
                ))
    }

    fun resetLiveData() {
        passwordChangeLiveData.value = null
    }


    companion object {

        private val TAG = ChangePasswordViewModel::class.java.simpleName
    }

}