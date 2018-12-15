package com.sasaj.graphics.drawingapp.authentication


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.authentication.states.LoginViewState
import com.sasaj.graphics.drawingapp.authentication.viewmodels.AuthenticationNavigationViewModel
import com.sasaj.graphics.drawingapp.authentication.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var vmLogin: LoginViewModel
    private lateinit var vmNavigation: AuthenticationNavigationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vmLogin = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        activity?.let {
            vmNavigation = ViewModelProviders.of(it).get(AuthenticationNavigationViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vmLogin.loginLiveData.observe(this, Observer { loginViewState -> handleViewState(loginViewState!!) })
        vmLogin.loginLiveData.observe(this, Observer {
            if (it != null) handleViewState(it)
        })
        vmLogin.errorState.observe(this, Observer { throwable ->
            throwable?.let {
                vmNavigation.error(it)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginButton!!.setOnClickListener {
            if (username?.text != null && username.text.toString() != "" && password?.text != null && password.text.toString() != "") {
                vmNavigation.loadingData()
                vmLogin.logIn(username?.text.toString(), password?.text.toString())
            } else
                vmNavigation.error(RuntimeException("Username and password must not be empty!"))
        }

        goToRegister!!.setOnClickListener {
            vmNavigation.goToRegister()
        }

        goToForgotPassword!!.setOnClickListener {
            vmNavigation.goToForgotPassword()
        }
    }


    private fun handleViewState(loginViewState: LoginViewState) {
        if (loginViewState.loading)
            vmNavigation.loadingData()
        else if (loginViewState.username != "") {
            Log.i(TAG, "Succesfully logged in")
            vmNavigation.loginSuccessful(loginViewState.username)
        }
    }

    companion object {
        private val TAG = LoginFragment::class.java.simpleName
    }
}
