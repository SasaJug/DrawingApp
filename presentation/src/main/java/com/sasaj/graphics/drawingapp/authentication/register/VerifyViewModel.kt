package com.sasaj.graphics.drawingapp.authentication.register

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sasaj.domain.usecases.VerifyUser
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import com.sasaj.graphics.drawingapp.common.UIException
import javax.inject.Inject

class VerifyViewModel(private val verifyUserUseCase: VerifyUser) : BaseViewModel() {

    val verifyLiveData: MutableLiveData<VerifyViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<UIException> = SingleLiveEvent()

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
                        { e -> errorState.value = UIException( cause = e) },
                        { Log.i(TAG, "Verification completed") }
                )
        )
    }

    companion object {
        private val TAG = VerifyViewModel::class.java.simpleName
    }

}