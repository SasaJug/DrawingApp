package com.sasaj.graphics.drawingapp.authentication.login

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sasaj.domain.usecases.LogIn
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent

class LoginViewModel(private val logInUseCase: LogIn) : BaseViewModel() {

    val loginLiveData: MutableLiveData<LoginViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    init {
        val loginViewState = LoginViewState()
        loginLiveData.value = loginViewState
    }

    fun logIn(username: String, password: String = "") {
        loginLiveData.value = loginLiveData.value?.copy(loading = true, username = "")
        addDisposable(logInUseCase.logIn(username, password)
                .subscribe(
                        { s: String ->
                            val newLoginViewState = loginLiveData.value?.copy(loading = false, username = s)
                            loginLiveData.value = newLoginViewState
                            errorState.value = null
                        },
                        { e ->
                            loginLiveData.value = loginLiveData.value?.copy(loading = false, username = "")
                            errorState.value = e
                        },
                        { Log.i(TAG, "Login completed") }
                )
        )
    }

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }
}