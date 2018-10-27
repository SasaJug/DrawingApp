package com.sasaj.graphics.drawingapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sasaj.graphics.drawingapp.viewmodel.common.Response
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.AuthRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RegisterViewModel : BaseViewModel() {

    @Inject
    lateinit var repo: AuthRepository

    private val registerLiveData: MutableLiveData<Response> = MutableLiveData()

    fun getRegisterLiveData(): MutableLiveData<Response> {
        return registerLiveData
    }

    private var disposable: Disposable? = repo.getRegisterSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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

    fun register (username: String, password: String = "", attrs : HashMap<String, String>) {
        registerLiveData.postValue(Response.loading())
        repo.signUp(username, password, attrs)
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