package com.sasaj.graphics.drawingapp.authentication.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.sasaj.domain.usecases.LogIn
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.authentication.AuthenticationNavigationViewModel
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import com.sasaj.graphics.drawingapp.common.UIException
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

data class LoginViewState(
    var loading : Boolean = false,
    var completed : Boolean = false
)

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val vmLogin by viewModels<LoginViewModel>()
    private val vmNavigation by activityViewModels<AuthenticationNavigationViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vmLogin.loginLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) handleViewState(it)
        })
        vmLogin.errorState.observe(this, Observer { customUIException ->
            handleError(customUIException)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginButton!!.setOnClickListener {
                vmLogin.logIn(username?.text.toString(), password?.text.toString())
        }

        goToRegister!!.setOnClickListener {
            vmNavigation.goToRegister()
        }

        goToForgotPassword!!.setOnClickListener {
            vmNavigation.goToForgotPassword()
        }
    }

    private fun handleViewState(loginViewState: LoginViewState) {
        resetErrors()
        if (loginViewState.loading)
            showProgress(true)
        else if (loginViewState.completed) {
            showProgress(false)
            Log.i(TAG, "Succesfully logged in")
            vmNavigation.goToMain()
        }
    }

    private fun handleError(customUIException: UIException?) {
        showProgress(false)
        resetErrors()
        if (customUIException?.errorCode!! > 0) {
            when {
                customUIException.errorCode == UIException.EMPTY_USERNAME + UIException.EMPTY_PASSWORD -> {
                    showUsernameError(getString(R.string.username_missing_error_message))
                    showPasswordError(getString(R.string.password_missing_error_message))
                }
                customUIException.errorCode == UIException.EMPTY_USERNAME -> showUsernameError(getString(R.string.username_missing_error_message))
                else -> showPasswordError(getString(R.string.password_missing_error_message))
            }
        } else {
            vmNavigation.error(customUIException)
        }
    }

    private fun showProgress(show: Boolean) {
        if (show)
            loginProgress.visibility = VISIBLE
        else
            loginProgress.visibility = GONE
    }

    private fun showUsernameError(message:String?){
        usernameLayout.error = message
    }

    private fun showPasswordError(message:String?){
        passwordLayout.error = message
    }


    private fun resetErrors(){
        showUsernameError(null)
        showPasswordError(null)
    }

    companion object {
        private val TAG = LoginFragment::class.java.simpleName
    }
}

@HiltViewModel
class LoginViewModel @Inject constructor(private val logInUseCase: LogIn) : BaseViewModel() {

    val loginLiveData: MutableLiveData<LoginViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<UIException?> = SingleLiveEvent()

    init {
        val loginViewState = LoginViewState()
        loginLiveData.value = loginViewState
    }

    fun logIn(username: String, password: String) {

        if (username.trim() == "" || password.trim() == "") {
            var errorCode = 0
            loginLiveData.value = loginLiveData.value?.copy(loading = false, completed = false)
            if (username.trim() == "") {
                errorCode = errorCode or UIException.EMPTY_USERNAME
            }
            if (password.trim() == "") {
                errorCode = errorCode or UIException.EMPTY_PASSWORD
            }
            errorState.value = UIException("Username and password must be provided", IllegalArgumentException(), errorCode)
            return
        }

        loginLiveData.value = loginLiveData.value?.copy(loading = true, completed = false)
        addDisposable(logInUseCase.logIn(username, password)
            .subscribe(
                { b: Boolean ->
                    val newLoginViewState = loginLiveData.value?.copy(loading = false, completed = true)
                    loginLiveData.value = newLoginViewState
                    errorState.value = null
                },
                { e ->
                    loginLiveData.value = loginLiveData.value?.copy(loading = false, completed = false)
                    errorState.value = UIException(cause = e)
                },
                { Log.i(TAG, "Login completed") }
            )
        )
    }

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }
}
