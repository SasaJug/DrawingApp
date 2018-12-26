package com.sasaj.graphics.drawingapp.authentication.register

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sasaj.domain.usecases.SignUp
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent

class RegisterViewModel(private val signUpUseCase : SignUp) : BaseViewModel() {

    val registerLiveData: MutableLiveData<RegisterViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    init {
        registerLiveData.value = RegisterViewState()
    }

    fun register (username: String, password: String = "", email : String = "") {
        var registerViewState = registerLiveData.value?.copy(registrationStarted = true, loading = true, isConfirmed = false)
        registerLiveData.value = registerViewState

        addDisposable(signUpUseCase.signUp(username, password, email)
                .subscribe(
                        { s: Boolean ->
                            val newRegisterViewState = registerLiveData.value?.copy(loading = false, isConfirmed = s)
                            registerLiveData.value = newRegisterViewState
                            errorState.value = null
                        },
                        { e ->
                            errorState.value = e
                        },
                        { Log.i(TAG, "Registration completed") }
                )
        )
    }

    companion object {
        private val TAG = RegisterViewModel::class.java.simpleName
    }

}