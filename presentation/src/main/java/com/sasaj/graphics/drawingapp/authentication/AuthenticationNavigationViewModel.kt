package com.sasaj.graphics.drawingapp.authentication

import android.arch.lifecycle.MutableLiveData
import com.sasaj.graphics.drawingapp.authentication.AuthenticationNavigationViewState
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent

class AuthenticationNavigationViewModel : BaseViewModel(){

    val navigationLiveData: MutableLiveData<AuthenticationNavigationViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    init {
        val authenticationNavigationViewState = AuthenticationNavigationViewState()
        navigationLiveData.value = authenticationNavigationViewState
    }

    fun loadingData(){
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.LOADING)
        navigationLiveData.value = newNavigationState
        errorState.value = null
    }

    fun loginSuccessful(username: String){
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.LOGIN_SUCCESSFUL, data = username)
        navigationLiveData.value = newNavigationState
        errorState.value = null
    }

    fun registerConfirmed(){
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.REGISTRATION_CONFIRMED)
        navigationLiveData.value = newNavigationState
        errorState.value = null
    }

    fun registerNotConfirmed(){
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.REGISTRATION_NOT_CONFIRMED)
        navigationLiveData.value = newNavigationState
        errorState.value = null
    }

    fun verifyConfirmed() {
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.VERIFICATION_CONFIRMED)
        navigationLiveData.value = newNavigationState
        errorState.value = null
    }

    fun error(throwable: Throwable){
        errorState.value = throwable
    }

    fun goToRegister() {
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.GO_TO_REGISTER)
        navigationLiveData.value = newNavigationState
    }

    fun goToForgotPassword() {
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.GO_TO_FORGOT_PASSWORD)
        navigationLiveData.value = newNavigationState    }

    fun getCode(username: String) {
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.GO_TO_NEW_PASSWORD, data = username)
        navigationLiveData.value = newNavigationState
    }

    fun passwordChangeRequested() {
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.PASSWORD_CHANGE_REQUESTED)
        navigationLiveData.value = newNavigationState
    }

    fun passwordChangeSuccessful() {
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.PASSWORD_CHANGE_SUCCESSFUL)
        navigationLiveData.value = newNavigationState
    }

}