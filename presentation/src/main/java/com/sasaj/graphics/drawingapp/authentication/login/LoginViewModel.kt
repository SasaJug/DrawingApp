package com.sasaj.graphics.drawingapp.authentication.login

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sasaj.domain.usecases.LogIn
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import com.sasaj.graphics.drawingapp.common.UIException

class LoginViewModel(private val logInUseCase: LogIn) : BaseViewModel() {

    val loginLiveData: MutableLiveData<LoginViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<UIException?> = SingleLiveEvent()

    init {
        val loginViewState = LoginViewState()
        loginLiveData.value = loginViewState
    }

    fun logIn(username: String, password: String) {

        if (username.trim() == "" || password.trim() == "") {
            var errorCode = 0
            loginLiveData.value = loginLiveData.value?.copy(loading = false, completed = false)
            if (username.trim() == "") {
                errorCode = errorCode or UIException.EMPTY_USERNAME
            }
            if (password.trim() == "") {
                errorCode = errorCode or UIException.EMPTY_PASSWORD
            }
            errorState.value = UIException("Username and password must be provided", IllegalArgumentException(), errorCode)
            return
        }

        loginLiveData.value = loginLiveData.value?.copy(loading = true, completed = false)
        addDisposable(logInUseCase.logIn(username, password)
                .subscribe(
                        { b: Boolean ->
                            val newLoginViewState = loginLiveData.value?.copy(loading = false, completed = true)
                            loginLiveData.value = newLoginViewState
                            errorState.value = null
                        },
                        { e ->
                            loginLiveData.value = loginLiveData.value?.copy(loading = false, completed = false)
                            errorState.value = UIException(cause = e)
                        },
                        { Log.i(TAG, "Login completed") }
                )
        )
    }

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }
}