package com.sasaj.graphics.drawingapp.authentication.register

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sasaj.graphics.drawingapp.DrawingApplication
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.authentication.AuthenticationNavigationViewModel
import com.sasaj.graphics.drawingapp.common.UIException
import kotlinx.android.synthetic.main.fragment_register.*
import javax.inject.Inject


class RegisterFragment : androidx.fragment.app.Fragment() {

    @Inject
    lateinit var registerVMFactory: RegisterVMFactory

    private lateinit var vmRegister: RegisterViewModel
    private lateinit var vmNavigation: AuthenticationNavigationViewModel

    //region lifecycle callbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as DrawingApplication).createRegisterComponent().inject(this)

        vmRegister = ViewModelProviders.of(this, registerVMFactory).get(RegisterViewModel::class.java)
        activity?.let {
            vmNavigation = ViewModelProviders.of(it).get(AuthenticationNavigationViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vmRegister.registerLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) handleViewState(it)
        })
        vmRegister.errorState.observe(viewLifecycleOwner, Observer { uiException ->
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

    override fun onDestroy() {
        (activity?.application as DrawingApplication).releaseRegisterComponent()
        super.onDestroy()
    }
    //endregion

    //region view state and error handlers
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
    //endregion

    //region rendering
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
    //endregion

    companion object {
        private val TAG = RegisterFragment::class.java.simpleName
    }
}
