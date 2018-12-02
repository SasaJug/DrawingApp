package com.sasaj.graphics.drawingapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sasaj.graphics.drawingapp.viewmodel.common.Response
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.AuthRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel : BaseViewModel() {

    @Inject
    lateinit var repo: AuthRepository

    private val loginLiveData: MutableLiveData<Response> = MutableLiveData()

    private val disposable: Disposable? = repo.getLoginSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    { s: String ->
                        if (s == "success") {
                            loginLiveData.setValue(Response.success(s))
                        } else {
                            loginLiveData.setValue(Response.success(""))
                        }
                    },
                    { e ->
                        Log.e(TAG, "onError", e)
                        loginLiveData.setValue(Response.error(e))
                    },
                    { Log.e(TAG, "onComplete") }
            )


    fun getLoginLiveData(): MutableLiveData<Response> {
        return loginLiveData
    }

    fun logIn(username: String, password: String = "") {
        loginLiveData.postValue(Response.loading())
        repo.logIn(username, password)
    }

    fun resetLiveData() {
        loginLiveData.value = null
    }

    override fun onCleared() {
        Log.e(TAG, "onCleared")
        disposable?.dispose()
        super.onCleared()
    }

    companion object {

        private val TAG = LoginViewModel::class.java.simpleName
    }
}