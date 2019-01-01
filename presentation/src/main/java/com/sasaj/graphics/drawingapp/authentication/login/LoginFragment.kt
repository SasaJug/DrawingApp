package com.sasaj.graphics.drawingapp.authentication.login


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.sasaj.graphics.drawingapp.DrawingApplication
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.authentication.AuthenticationNavigationViewModel
import com.sasaj.graphics.drawingapp.common.UIException
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : Fragment() {

    @Inject
    lateinit var loginVMFactory: LoginVMFactory

    private lateinit var vmLogin: LoginViewModel
    private lateinit var vmNavigation: AuthenticationNavigationViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity?.application as DrawingApplication).createLoginComponent().inject(this)


        vmLogin = ViewModelProviders.of(this, loginVMFactory).get(LoginViewModel::class.java)
        activity?.let {
            vmNavigation = ViewModelProviders.of(it).get(AuthenticationNavigationViewModel::class.java)
        }

        vmLogin.loginLiveData.observe(this, Observer {
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
        showUsernameError(null)
        showPasswordError(null)
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
            showUsernameError(null)
            showPasswordError(null)
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
        username.error = message
    }

    private fun showPasswordError(message:String?){
        password.error = message
    }

    override fun onDestroy() {
        (activity?.application as DrawingApplication).releaseLoginComponent()
        super.onDestroy()
    }

    companion object {
        private val TAG = LoginFragment::class.java.simpleName
    }
}
