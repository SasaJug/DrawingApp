package com.sasaj.graphics.drawingapp.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.authentication.changepassword.ForgotPasswordFragment
import com.sasaj.graphics.drawingapp.authentication.changepassword.NewPasswordFragment
import com.sasaj.graphics.drawingapp.authentication.login.LoginFragment
import com.sasaj.graphics.drawingapp.authentication.register.RegisterFragment
import com.sasaj.graphics.drawingapp.authentication.register.VerifyFragment
import com.sasaj.graphics.drawingapp.common.BaseActivity
import com.sasaj.graphics.drawingapp.common.UIException
import com.sasaj.graphics.drawingapp.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
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
    }

    private fun goToForgotPasswordFragment() {
        hideProgress()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, ForgotPasswordFragment(), "forgotPassword")
                .commitNow()
    }

    private fun goToVerify() {
        hideProgress()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, VerifyFragment(), "verify")
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
