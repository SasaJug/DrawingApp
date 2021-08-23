//package com.sasaj.graphics.drawingapp.authentication.register
//
//import androidx.lifecycle.MutableLiveData
//import android.util.Log
//import com.sasaj.domain.usecases.SignUp
//import com.sasaj.graphics.drawingapp.common.BaseViewModel
//import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
//import com.sasaj.graphics.drawingapp.common.UIException
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject
//
//@HiltViewModel
//class RegisterViewModel @Inject constructor(private val signUpUseCase: SignUp) : BaseViewModel() {
//
//    val registerLiveData: MutableLiveData<RegisterViewState> = MutableLiveData()
//    var errorState: SingleLiveEvent<UIException> = SingleLiveEvent()
//
//    init {
//        registerLiveData.value = RegisterViewState()
//    }
//
//    fun register(username: String, email: String, password: String, confirmPassword: String) {
//        var errorCode = 0
//
//        if (username.trim() == "")
//            errorCode = errorCode or UIException.EMPTY_USERNAME
//        if (email.trim() == "")
//            errorCode = errorCode or UIException.EMPTY_EMAIL
//        if (password.trim() == "")
//            errorCode = errorCode or UIException.EMPTY_PASSWORD
//        if (confirmPassword.trim() == "")
//            errorCode = errorCode or UIException.EMPTY_CONFIRM_PASSWORD
//        if (password != confirmPassword)
//            errorCode = errorCode or UIException.PASSWORDS_DO_NOT_MATCH
//
//        if (errorCode > 0) {
//            errorState.value = UIException("All entries must be valid", IllegalArgumentException(), errorCode)
//            return
//        }
//
//        val registerViewState = registerLiveData.value?.copy(registrationStarted = true, loading = true, isConfirmed = false)
//        registerLiveData.value = registerViewState
//
//        addDisposable(signUpUseCase.signUp(username, password, email)
//                .subscribe(
//                        { b: Boolean ->
//                            val newRegisterViewState = registerLiveData.value?.copy(loading = false, isConfirmed = b)
//                            registerLiveData.value = newRegisterViewState
//                        },
//                        { e ->
//                            errorState.value = UIException(cause = e)
//                        },
//                        { Log.i(TAG, "Registration started") }
//                )
//        )
//    }
//
//    companion object {
//        private val TAG = RegisterViewModel::class.java.simpleName
//    }
//
//}