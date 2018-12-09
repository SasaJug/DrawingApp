package com.sasaj.graphics.drawingapp

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.sasaj.graphics.drawingapp.authentication.AuthenticationNavigationViewModel
import com.sasaj.graphics.drawingapp.authentication.LoginFragment
import com.sasaj.graphics.drawingapp.authentication.states.AuthenticationNavigationViewState
import com.sasaj.graphics.drawingapp.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*



class AuthenticationActivity : BaseActivity() {

    private lateinit var vm: AuthenticationNavigationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        setSupportActionBar(toolbar)

        vm = ViewModelProviders.of(this).get(AuthenticationNavigationViewModel::class.java)
        vm.navigationLiveData.observe(this, Observer { navigationState -> handleResponse(navigationState) })
        vm.errorState.observe(this, Observer { throwable ->
            throwable?.let {
                renderErrorState(it)
            }
        })

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, LoginFragment(), "login")
                    .commitNow()
        }
    }

    private fun handleResponse(navigationState: AuthenticationNavigationViewState?) {
        when(navigationState?.state){
            AuthenticationNavigationViewState.LOADING -> renderLoadingState()
            AuthenticationNavigationViewState.LOGIN_SUCCESFUL -> renderSucessLoggingInState(navigationState.data)
        }
    }

    private fun renderLoadingState() {
        showProgress("wait...")
    }

    private fun renderSucessLoggingInState(username: String?) {
        hideProgress()
        if (username != "") {
            val intent = Intent(this@AuthenticationActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun renderErrorState(throwable: Throwable?) {
        hideProgress()
        Log.e(TAG, "Error logging in ", throwable)
        showDialogMessage("Error logging in", throwable.toString())
    }

    companion object {
        private val TAG = AuthenticationActivity::class.java.simpleName
    }
}
