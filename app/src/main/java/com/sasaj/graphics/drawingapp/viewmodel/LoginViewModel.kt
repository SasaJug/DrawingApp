package com.sasaj.graphics.drawingapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.sasaj.graphics.drawingapp.viewmodel.common.Response
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.AuthRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel : BaseViewModel() {

    @Inject
    lateinit var repo: AuthRepository

    private val response: MutableLiveData<Response> = MutableLiveData()

    fun response(): MutableLiveData<Response> {
        return response
    }

    fun logIn(username: String, password: String = "") {
        repo.getAuthenticationSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { response.postValue(Response.loading()) }
                .subscribe(
                        { s: String ->
                            if (s != "") {
                                response.setValue(Response.success(s))
                            } else {
                                response.setValue(Response.success(""))
                            }
                        },
                        { e -> response.setValue(Response.error(e)) },
                        { {} }
                )
        repo.logIn(username, password)
    }

}