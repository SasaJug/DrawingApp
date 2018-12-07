package com.sasaj.graphics.drawingapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sasaj.domain.usecases.VerifyUser
import com.sasaj.graphics.drawingapp.viewmodel.common.Response
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.AuthRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class VerifyViewModel : BaseViewModel(){

    @Inject
    lateinit var verifyUserUseCase : VerifyUser

    private val verifyLiveData: MutableLiveData<Response> = MutableLiveData()

    fun getVerifyLiveData(): MutableLiveData<Response> {
        return verifyLiveData
    }

    private var disposable: Disposable? = null


    fun verify(username: String?, code: String?) {
        verifyLiveData.postValue(Response.loading())
        disposable = verifyUserUseCase.verifyUser(username!!, code!!)
                .subscribe(
                        { b : Boolean ->
                            if (b) {
                                verifyLiveData.setValue(Response.success("verified"))
                            } else {
                                verifyLiveData.setValue(Response.success("notVerified"))
                            }
                        },
                        { e -> verifyLiveData.setValue(Response.error(e)) },
                        { Log.i(TAG, "completed") }
                )
    }

    fun resetLiveData(){
        verifyLiveData.value = null
    }

    override fun onCleared() {
        Log.i(TAG, "onCleared")
        disposable?.dispose()
        super.onCleared()
    }

    companion object {
        private val TAG = VerifyViewModel::class.java.simpleName
    }

}