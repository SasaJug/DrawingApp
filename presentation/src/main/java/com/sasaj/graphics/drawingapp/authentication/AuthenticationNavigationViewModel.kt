package com.sasaj.graphics.drawingapp.authentication

import android.arch.lifecycle.MutableLiveData
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import com.sasaj.graphics.drawingapp.common.UIException

class AuthenticationNavigationViewModel : BaseViewModel(){

    val navigationLiveData: MutableLiveData<AuthenticationNavigationViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<UIException> = SingleLiveEvent()

    init {
        val authenticationNavigationViewState = AuthenticationNavigationViewState()
        navigationLiveData.value = authenticationNavigationViewState
    }

    fun error(uiException: UIException){
        errorState.value = uiException
    }

    fun goToMain() {
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.GO_TO_MAIN)
        navigationLiveData.value = newNavigationState
    }

    fun goToRegister() {
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.GO_TO_REGISTER)
        navigationLiveData.value = newNavigationState
    }

    fun goToVerifyRegistration() {
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.GO_TO_VERIFY)
        navigationLiveData.value = newNavigationState
    }

    fun goToForgotPassword() {
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.GO_TO_FORGOT_PASSWORD)
        navigationLiveData.value = newNavigationState    }

    fun getCode(username: String) {
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.GO_TO_NEW_PASSWORD, data = username)
        navigationLiveData.value = newNavigationState
    }
}