package com.sasaj.graphics.drawingapp.authentication

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.authentication.changepassword.ForgotPaswordFragment
import com.sasaj.graphics.drawingapp.authentication.changepassword.NewPasswordFragment
import com.sasaj.graphics.drawingapp.authentication.login.LoginFragment
import com.sasaj.graphics.drawingapp.authentication.register.RegisterFragment
import com.sasaj.graphics.drawingapp.authentication.register.VerifyFragment
import com.sasaj.graphics.drawingapp.common.BaseActivity
import com.sasaj.graphics.drawingapp.common.UIException
import com.sasaj.graphics.drawingapp.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*


class AuthenticationActivity : BaseActivity() {

    private lateinit var vm: AuthenticationNavigationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        setSupportActionBar(toolbar)

        vm = ViewModelProviders.of(this).get(AuthenticationNavigationViewModel::class.java)
        vm.navigationLiveData.observe(this, Observer { navigationState -> handleResponse(navigationState) })
        vm.errorState.observe(this, Observer { uiException ->
            uiException .let {
                renderErrorState(it!!)
            }
        })

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, LoginFragment(), "login")
                    .commitNow()
        }
    }

    private fun handleResponse(navigationState: AuthenticationNavigationViewState?) {
        when (navigationState?.state) {
            AuthenticationNavigationViewState.LOADING -> renderLoadingState()
            AuthenticationNavigationViewState.REGISTRATION_CONFIRMED -> renderRegistrationConfirmedState()
            AuthenticationNavigationViewState.REGISTRATION_NOT_CONFIRMED -> renderRegistrationNotConfirmedState()
            AuthenticationNavigationViewState.VERIFICATION_CONFIRMED -> renderVerificationConfirmedState()

            AuthenticationNavigationViewState.GO_TO_MAIN -> goToMain()
            AuthenticationNavigationViewState.GO_TO_REGISTER -> goToRegister()
            AuthenticationNavigationViewState.GO_TO_FORGOT_PASSWORD -> goToForgotPasswordFragment()
            AuthenticationNavigationViewState.GO_TO_NEW_PASSWORD -> goToNewPassword(navigationState.data)
            AuthenticationNavigationViewState.PASSWORD_CHANGE_REQUESTED -> renderChangePasswordRequested()
            AuthenticationNavigationViewState.PASSWORD_CHANGE_SUCCESSFUL -> renderChangePasswordSuccessful()
        }
    }


    private fun renderLoadingState() {
        showProgress()
    }

    private fun renderRegistrationConfirmedState() {
        hideProgress()
        val intent = Intent(this@AuthenticationActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun renderRegistrationNotConfirmedState() {
        hideProgress()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, VerifyFragment(), "verify")
                .commitNow()
    }

    private fun renderVerificationConfirmedState() {
        hideProgress()
        val intent = Intent(this@AuthenticationActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun renderChangePasswordRequested() {
        hideProgress()
    }

    private fun renderChangePasswordSuccessful() {
        hideProgress()
        val intent = Intent(this@AuthenticationActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
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
    }

    private fun goToForgotPasswordFragment() {
        hideProgress()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, ForgotPaswordFragment(), "forgotPassword")
                .commitNow()
    }

    private fun goToNewPassword(username: String) {
        hideProgress()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, NewPasswordFragment.newInstance(username), "newPassword")
                .commitNow()
    }

    companion object {
        private val TAG = AuthenticationActivity::class.java.simpleName
    }
}
