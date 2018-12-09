package com.sasaj.graphics.drawingapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sasaj.domain.usecases.LogIn
import com.sasaj.graphics.drawingapp.authentication.states.LoginViewState
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import javax.inject.Inject

class LoginViewModel : BaseViewModel() {

    @Inject
    lateinit var logInUseCase: LogIn

    private val loginLiveData: MutableLiveData<LoginViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    init {
        val loginViewState = LoginViewState()
        loginLiveData.value = loginViewState
    }

    fun getLoginLiveData(): MutableLiveData<LoginViewState> {
        return loginLiveData
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
                        { Log.e(TAG, "completed") }
                )
        )
    }

    fun resetLiveData() {
        loginLiveData.value = null
    }

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }
}