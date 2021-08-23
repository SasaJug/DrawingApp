package com.sasaj.graphics.drawingapp.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.authentication.changepassword.ForgotPasswordFragment
import com.sasaj.graphics.drawingapp.authentication.changepassword.NewPasswordFragment
import com.sasaj.graphics.drawingapp.authentication.login.LoginFragment
import com.sasaj.graphics.drawingapp.authentication.register.RegisterFragment
import com.sasaj.graphics.drawingapp.authentication.register.VerifyFragment
import com.sasaj.graphics.drawingapp.common.BaseActivity
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import com.sasaj.graphics.drawingapp.common.UIException
import com.sasaj.graphics.drawingapp.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

data class AuthenticationNavigationViewState(
    var state: Int = -1,
    var data: String = ""
) {
    companion object {
        const val LOADING: Int = 0

        const val LOGIN_SUCCESSFUL: Int = 1
        const val REGISTRATION_CONFIRMED: Int = 2
        const val REGISTRATION_NOT_CONFIRMED: Int = 3
        const val VERIFICATION_CONFIRMED: Int = 4
        const val PASSWORD_CHANGE_REQUESTED: Int = 5
        const val PASSWORD_CHANGE_SUCCESSFUL: Int = 6

        const val GO_TO_MAIN: Int = 12
        const val GO_TO_REGISTER: Int = 13
        const val GO_TO_FORGOT_PASSWORD: Int = 14
        const val GO_TO_VERIFY: Int = 15
        const val GO_TO_NEW_PASSWORD: Int = 16
    }

}

@AndroidEntryPoint
class AuthenticationActivity : BaseActivity() {

    private val vm by viewModels<AuthenticationNavigationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        setSupportActionBar(toolbar)

//        vm = ViewModelProviders.of(this).get(AuthenticationNavigationViewModel::class.java)
        vm.navigationLiveData.observe(this) { navigationState -> handleResponse(navigationState) }
        vm.errorState.observe(this) { uiException ->
            renderErrorState(uiException!!)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, LoginFragment(), "login")
                    .commitNow()
        }
        supportActionBar?.setTitle(R.string.login_title)
    }

    private fun handleResponse(navigationState: AuthenticationNavigationViewState?) {
        when (navigationState?.state) {

            AuthenticationNavigationViewState.GO_TO_MAIN -> goToMain()
            AuthenticationNavigationViewState.GO_TO_REGISTER -> goToRegister()
            AuthenticationNavigationViewState.GO_TO_VERIFY -> goToVerify()
            AuthenticationNavigationViewState.GO_TO_FORGOT_PASSWORD -> goToForgotPasswordFragment()
            AuthenticationNavigationViewState.GO_TO_NEW_PASSWORD -> goToNewPassword(navigationState.data)
        }
    }

    private fun renderErrorState(uiException: UIException) {
        hideProgress()
        Log.e(TAG, "Error ", uiException.cause)
        showDialogMessage("Error ", uiException.cause.toString())
    }

    private fun goToMain() {
        hideProgress()
        val intent = Intent(this@AuthenticationActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToRegister() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, RegisterFragment(), "register")
                .commitNow()
        supportActionBar?.setTitle(R.string.register_title)
    }

    private fun goToForgotPasswordFragment() {
        hideProgress()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, ForgotPasswordFragment(), "forgotPassword")
                .commitNow()
        supportActionBar?.setTitle(R.string.forgot_password_title)
    }

    private fun goToVerify() {
        hideProgress()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, VerifyFragment(), "verify")
                .commitNow()
        supportActionBar?.setTitle(R.string.enter_code_title)
    }

    private fun goToNewPassword(username: String) {
        hideProgress()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, NewPasswordFragment.newInstance(username), "newPassword")
                .commitNow()
        supportActionBar?.setTitle(R.string.enter_new_password_title)
    }

    companion object {
        private val TAG = AuthenticationActivity::class.java.simpleName
    }
}


@HiltViewModel
class AuthenticationNavigationViewModel @Inject constructor() : BaseViewModel(){

    val navigationLiveData: MutableLiveData<AuthenticationNavigationViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<UIException> = SingleLiveEvent()

    init {
        val authenticationNavigationViewState = AuthenticationNavigationViewState()
        navigationLiveData.value = authenticationNavigationViewState
    }

    fun error(uiException: UIException){
        errorState.value = uiException
    }

    fun goToMain() {
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.GO_TO_MAIN)
        navigationLiveData.value = newNavigationState
    }

    fun goToRegister() {
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.GO_TO_REGISTER)
        navigationLiveData.value = newNavigationState
    }

    fun goToVerifyRegistration() {
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.GO_TO_VERIFY)
        navigationLiveData.value = newNavigationState
    }

    fun goToForgotPassword() {
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.GO_TO_FORGOT_PASSWORD)
        navigationLiveData.value = newNavigationState    }

    fun getCode(username: String) {
        val newNavigationState = navigationLiveData.value?.copy(state = AuthenticationNavigationViewState.GO_TO_NEW_PASSWORD, data = username)
        navigationLiveData.value = newNavigationState
    }
}
