package com.sasaj.graphics.drawingapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import com.sasaj.domain.usecases.ChangePassword
import com.sasaj.domain.usecases.NewPassword
import com.sasaj.graphics.drawingapp.ui.authentication.LoginActivity
import com.sasaj.graphics.drawingapp.ui.main.MainActivity
import com.sasaj.graphics.drawingapp.viewmodel.common.Response
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class ChangePasswordViewModel : BaseViewModel(){

    @Inject
    lateinit var changePasswordUseCase : ChangePassword

    @Inject
    lateinit var newPasswordUseCase : NewPassword

    private val changePasswordLiveData: MutableLiveData<Response> = MutableLiveData()

    private var disposable: Disposable? = null

    fun getChangePasswordLiveData(): MutableLiveData<Response> {
        return changePasswordLiveData
    }

    fun changePassword(username : String){
       addDisposable(changePasswordUseCase.changePassword(username)
                .subscribe(
                        { s: Boolean->
                            if (s) {
                                changePasswordLiveData.setValue(Response.success("success change password"))
                            } else {
                                changePasswordLiveData.setValue(Response.success(""))
                            }
                        },
                        { e ->
                            Log.e(TAG, "onError", e)
                            changePasswordLiveData.setValue(Response.error(e))
                        },
                        { Log.e(TAG, "onComplete") }
                ))
    }


    fun newPassword(password : String, code : String){
       addDisposable(newPasswordUseCase.newPassword(password, code)
                .subscribe(
                        { s: Boolean->
                            if (s) {
                                changePasswordLiveData.setValue(Response.success("success new password"))
                            } else {
                                changePasswordLiveData.setValue(Response.success(""))
                            }
                        },
                        { e ->
                            Log.e(TAG, "onError", e)
                            changePasswordLiveData.setValue(Response.error(e))
                        },
                        { Log.e(TAG, "onComplete") }
                ))
    }

    fun resetLiveData() {
        changePasswordLiveData.value = null
    }


    companion object {

        private val TAG = ChangePasswordViewModel::class.java.simpleName
    }

}