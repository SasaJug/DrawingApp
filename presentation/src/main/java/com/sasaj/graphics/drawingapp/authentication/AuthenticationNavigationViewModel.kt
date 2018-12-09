package com.sasaj.graphics.drawingapp.authentication

import android.arch.lifecycle.MutableLiveData
import com.sasaj.graphics.drawingapp.authentication.states.AuthenticationNavigationViewState
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
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.LOGIN_SUCCESFUL, data = username)
        navigationLiveData.value = newNavigationState
        errorState.value = null
    }

    fun error(throwable: Throwable){
        errorState.value = throwable
    }

}