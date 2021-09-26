package com.sasaj.graphics.drawingapp.authentication.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.sasaj.domain.usecases.SignUp
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.authentication.AuthenticationNavigationViewModel
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import com.sasaj.graphics.drawingapp.common.UIException
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.synthetic.main.fragment_register.*
import javax.inject.Inject

data class RegisterViewState(
    var registrationStarted : Boolean  = false,
    var loading : Boolean = false,
    var isConfirmed: Boolean = false
)

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val vmRegister by viewModels<RegisterViewModel>()
    private val vmNavigation by activityViewModels<AuthenticationNavigationViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vmRegister.registerLiveData.observe(viewLifecycleOwner, Observer{
            if (it != null) handleViewState(it)
        })
        vmRegister.errorState.observe(viewLifecycleOwner, Observer{ uiException ->
            handleError(uiException)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpButton!!.setOnClickListener {
            vmRegister.register(
                    usernameRegister?.text.toString(),
                    emailRegister?.text.toString(),
                    passwordRegister?.text.toString(),
                    confirmPasswordRegister?.text.toString())
        }
    }

    private fun handleViewState(registerViewState: RegisterViewState) {
        when {
            registerViewState.registrationStarted.not() -> return
            registerViewState.loading -> showProgress(true)
            registerViewState.isConfirmed -> {
                showProgress(false)
                Log.i(TAG, "Registration Confirmed")
                vmNavigation.goToMain()
            }
            else -> {
                showProgress(false)
                Log.i(TAG, "Registration NotConfirmed")
                vmNavigation.goToVerifyRegistration()
            }
        }
    }

    private fun handleError(customUIException: UIException?) {
        showProgress(false)
        resetErrors()
        if (customUIException?.errorCode!! > 0) {
            val code = customUIException.errorCode
            if (code and UIException.EMPTY_USERNAME == UIException.EMPTY_USERNAME)
                showUsernameError(getString(R.string.username_missing_error_message))
            if (code and (UIException.EMPTY_EMAIL) == UIException.EMPTY_EMAIL)
                showEmailError(getString(R.string.email_missing_error_message))
            if (code and (UIException.EMPTY_PASSWORD) == UIException.EMPTY_PASSWORD)
                showPasswordError(getString(R.string.password_missing_error_message))
            if (code and (UIException.EMPTY_CONFIRM_PASSWORD) == UIException.EMPTY_CONFIRM_PASSWORD)
                showConfirmPasswordError(getString(R.string.confirm_password_missing_error_message))
            if (code and (UIException.PASSWORDS_DO_NOT_MATCH) == UIException.PASSWORDS_DO_NOT_MATCH) {
                showPasswordError(getString(R.string.passwords_do_not_match_error))
                showConfirmPasswordError(getString(R.string.passwords_do_not_match_error))
            }
        } else {
            vmNavigation.error(customUIException)
        }
    }

    private fun showProgress(show: Boolean) {
        if (show)
            registerProgress.visibility = View.VISIBLE
        else
            registerProgress.visibility = View.GONE
    }

    private fun showUsernameError(message: String?) {
        usernameRegisterLayout.error = message
    }

    private fun showEmailError(message: String?) {
        emailRegisterLayout.error = message
    }

    private fun showPasswordError(message: String?) {
        passwordRegisterLayout.error = message
    }

    private fun showConfirmPasswordError(message: String?) {
        confirmPasswordRegisterLayout.error = message
    }

    private fun resetErrors(){
        showUsernameError(null)
        showEmailError(null)
        showPasswordError(null)
        showConfirmPasswordError(null)
    }

    companion object {
        private val TAG = RegisterFragment::class.java.simpleName
    }
}


@HiltViewModel
class RegisterViewModel @Inject constructor(private val signUpUseCase: SignUp) : BaseViewModel() {

    val registerLiveData: MutableLiveData<RegisterViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<UIException> = SingleLiveEvent()

    init {
        registerLiveData.value = RegisterViewState()
    }

    fun register(username: String, email: String, password: String, confirmPassword: String) {
        var errorCode = 0

        if (username.trim() == "")
            errorCode = errorCode or UIException.EMPTY_USERNAME
        if (email.trim() == "")
            errorCode = errorCode or UIException.EMPTY_EMAIL
        if (password.trim() == "")
            errorCode = errorCode or UIException.EMPTY_PASSWORD
        if (confirmPassword.trim() == "")
            errorCode = errorCode or UIException.EMPTY_CONFIRM_PASSWORD
        if (password != confirmPassword)
            errorCode = errorCode or UIException.PASSWORDS_DO_NOT_MATCH

        if (errorCode > 0) {
            errorState.value = UIException("All entries must be valid", IllegalArgumentException(), errorCode)
            return
        }

        val registerViewState = registerLiveData.value?.copy(registrationStarted = true, loading = true, isConfirmed = false)
        registerLiveData.value = registerViewState

        addDisposable(signUpUseCase.signUp(username, password, email)
            .subscribe(
                { b: Boolean ->
                    val newRegisterViewState = registerLiveData.value?.copy(loading = false, isConfirmed = b)
                    registerLiveData.value = newRegisterViewState
                },
                { e ->
                    errorState.value = UIException(cause = e)
                },
                { Log.i(TAG, "Registration started") }
            )
        )
    }

    companion object {
        private val TAG = RegisterViewModel::class.java.simpleName
    }

}