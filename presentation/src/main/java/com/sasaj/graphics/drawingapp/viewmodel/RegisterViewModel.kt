package com.sasaj.graphics.drawingapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sasaj.domain.usecases.SignUp
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.viewmodel.common.Response
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class RegisterViewModel : BaseViewModel() {

    @Inject
    lateinit var signUpUseCase : SignUp

    private val registerLiveData: MutableLiveData<Response> = MutableLiveData()

    fun getRegisterLiveData(): MutableLiveData<Response> {
        return registerLiveData
    }

    private var disposable: Disposable? = null


    fun register (username: String, password: String = "", email : String = "") {
        registerLiveData.postValue(Response.loading())
        signUpUseCase.signUp(username, password, email)
                .subscribe(
                        { b: Boolean ->
                            if (b) {
                                registerLiveData.setValue(Response.success("confirmed"))
                            } else {
                                registerLiveData.setValue(Response.success("notConfirmed"))
                            }
                        },
                        { e -> registerLiveData.setValue(Response.error(e)) },
                        { Log.e(TAG, "completed") }
                )
    }

    fun resetLiveData() {
        registerLiveData.value = null
    }

    override fun onCleared() {
        Log.e(TAG, "onCleared")
        disposable?.dispose()
        super.onCleared()
    }

    companion object {
        private val TAG = RegisterViewModel::class.java.simpleName
    }

}