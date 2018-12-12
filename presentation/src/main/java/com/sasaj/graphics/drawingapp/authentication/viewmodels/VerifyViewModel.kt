package com.sasaj.graphics.drawingapp.authentication.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sasaj.domain.usecases.VerifyUser
import com.sasaj.graphics.drawingapp.authentication.states.VerifyViewState
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import javax.inject.Inject

class VerifyViewModel : BaseViewModel() {

    @Inject
    lateinit var verifyUserUseCase: VerifyUser

    val verifyLiveData: MutableLiveData<VerifyViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    init {
        verifyLiveData.value = VerifyViewState()
    }

    fun verify(username: String?, code: String?) {
        val verifyViewState = verifyLiveData.value?.copy(verificationStarted = true, loading = true, isVerified = false)
        verifyLiveData.value = verifyViewState

        addDisposable(verifyUserUseCase.verifyUser(username!!, code!!)
                .subscribe(
                        { b: Boolean ->
                            if(b){
                                val newVerifyViewState = verifyLiveData.value?.copy(verificationStarted = true, loading = false, isVerified = b)
                                verifyLiveData.value = newVerifyViewState
                                errorState.value = null
                            }
                        },
                        { e -> errorState.value = e },
                        { Log.i(TAG, "Verification completed") }
                )
        )
    }

    companion object {
        private val TAG = VerifyViewModel::class.java.simpleName
    }

}