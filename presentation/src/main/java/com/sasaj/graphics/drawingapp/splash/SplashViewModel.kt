package com.sasaj.graphics.drawingapp.splash

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sasaj.domain.usecases.CheckIfLoggedIn
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import javax.inject.Inject

class SplashViewModel : BaseViewModel() {

    @Inject
    lateinit var checkIfLoggedIn: CheckIfLoggedIn

    private val splashLiveData: MutableLiveData<SplashViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    init {
        val splashViewState = SplashViewState()
        splashLiveData.value = splashViewState
    }

    fun getSplashLiveData(): MutableLiveData<SplashViewState> {
        return splashLiveData
    }

    fun checkIfLoggedIn() {
        addDisposable(checkIfLoggedIn.checkIfLoggedIn()
                .subscribe(
                        { s: String ->
                            val newSplashViewState = splashLiveData.value?.copy(loading = false, username = s)
                            splashLiveData.value = newSplashViewState
                            errorState.value = null
                        },
                        { e ->
                            splashLiveData.value = splashLiveData.value?.copy(loading = false, username = "")
                            errorState.value = e
                        },
                        { Log.i(TAG, "Check user logged in completed") }
                )
        )
    }


    companion object {
        private val TAG = SplashViewModel::class.java.simpleName
    }
}