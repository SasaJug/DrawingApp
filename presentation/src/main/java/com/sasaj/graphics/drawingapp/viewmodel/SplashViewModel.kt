package com.sasaj.graphics.drawingapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sasaj.domain.usecases.CheckIfLoggedIn
import com.sasaj.graphics.drawingapp.viewmodel.common.Response
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.AuthRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashViewModel : BaseViewModel() {

    @Inject
    lateinit var checkIfLoggedIn: CheckIfLoggedIn

    private val splashLiveData: MutableLiveData<Response> = MutableLiveData()

    fun getSplashLiveData(): MutableLiveData<Response> {
        return splashLiveData
    }

    private var disposable: Disposable? = null

    fun checkIfLoggedIn() {
        splashLiveData.postValue(Response.loading())
        disposable = checkIfLoggedIn.checkIfLoggedIn()
                .subscribe(
                        { s: String ->
                            if (s != "") {
                                splashLiveData.setValue(Response.success(s))
                            } else {
                                splashLiveData.setValue(Response.success(""))
                            }
                        },
                        { e -> splashLiveData.setValue(Response.error(e)) },
                        { Log.e(TAG, "completed") }
                )
    }

    override fun onCleared() {
        Log.i(TAG, "onCleared")
        disposable?.dispose()
        super.onCleared()
    }

    companion object {
        private val TAG = SplashViewModel::class.java.simpleName
    }
}