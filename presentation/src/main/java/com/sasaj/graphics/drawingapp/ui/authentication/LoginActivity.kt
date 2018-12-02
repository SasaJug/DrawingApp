package com.sasaj.graphics.drawingapp.ui.authentication

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.ui.base.BaseActivity
import com.sasaj.graphics.drawingapp.ui.main.MainActivity
import com.sasaj.graphics.drawingapp.viewmodel.LoginViewModel
import com.sasaj.graphics.drawingapp.viewmodel.common.Response
import com.sasaj.graphics.drawingapp.viewmodel.common.Status
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private lateinit var vm: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        vm = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        vm.getLoginLiveData().observe(this, Observer { response -> processResponse(response) })

        loginButton!!.setOnClickListener {
            if (username?.text != null && !username.equals("") && password?.text != null && !password.text.toString().equals(""))
                vm.logIn(username?.text.toString(), password?.text.toString())
            else
                showDialogMessage("Error", "Username and password must not be empty!")
        }

        goToRegister!!.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        goToForgotPassword!!.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun processResponse(response: Response?) {
        when (response?.status) {
            Status.LOADING -> renderLoadingState()
            Status.SUCCESS -> renderSucessLoggingInState(response.data)
            Status.ERROR -> renderErrorState(response.error)
        }
    }

    override fun resetViewModel() {
        vm.resetLiveData()
    }

    private fun renderLoadingState() {
        showProgress("wait...")
    }

    private fun renderSucessLoggingInState(username: String?) {
        hideProgress()
        if (username == "success") {
            Log.i(LoginActivity.TAG, "Succesfully logged in")
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            showDialogMessage("Error logging in", username)
        }
    }

    private fun renderErrorState(throwable: Throwable?) {
        hideProgress()
        Log.e(LoginActivity.TAG, "Error logging in ", throwable)
        showDialogMessage("Error logging in", throwable.toString())
    }


    companion object {

        private val TAG = LoginActivity::class.java.simpleName
    }
}
