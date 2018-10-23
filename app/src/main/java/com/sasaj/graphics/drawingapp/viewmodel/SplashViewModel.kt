package com.sasaj.graphics.drawingapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.sasaj.graphics.drawingapp.repository.AwsAuthRepositoryImplementation
import com.sasaj.graphics.drawingapp.viewmodel.common.Response
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashViewModel : BaseViewModel() {

    @Inject
    lateinit var repo: AwsAuthRepositoryImplementation

    private val response: MutableLiveData<Response> = MutableLiveData()

    fun response(): MutableLiveData<Response> {
        return response
    }

    fun isLoggedIn() {
        repo.getAuthenticationSubject()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { response.postValue(Response.loading())}
                .subscribe(
                        { s: String ->
                            if (s != "") {
                                response.postValue(Response.success(s))
                            } else {
                                response.postValue(Response.success(""))
                            }
                        },
                        { e -> response.postValue(Response.error(e)) },
                        { {} }
                )
        repo.isLoggedIn()
    }

}