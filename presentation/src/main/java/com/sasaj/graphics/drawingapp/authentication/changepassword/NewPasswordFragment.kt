package com.sasaj.graphics.drawingapp.authentication.changepassword

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.sasaj.graphics.drawingapp.DrawingApplication
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.authentication.AuthenticationNavigationViewModel
import com.sasaj.graphics.drawingapp.common.UIException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_new_password.*
import javax.inject.Inject

@AndroidEntryPoint
class NewPasswordFragment : Fragment() {
//
//    @Inject
//    lateinit var forgotPasswordVMFactory: ForgotPasswordVMFactory

    private var username: String? = null

    private val vmForgotPassword by viewModels<ForgotPasswordViewModel>()
    private val vmNavigation by activityViewModels<AuthenticationNavigationViewModel>()

    //region lifecycle callbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        (activity?.application as DrawingApplication).createForgotPasswordComponent().inject(this)

        arguments?.let {
            username = it.getString(USERNAME)
        }

//        vmForgotPassword = ViewModelProviders.of(this, forgotPasswordVMFactory).get(ForgotPasswordViewModel::class.java)
//        activity?.let {
//            vmNavigation = ViewModelProviders.of(it).get(AuthenticationNavigationViewModel::class.java)
//        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vmForgotPassword.forgotPasswordLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) handleViewState(it)
        })
        vmForgotPassword.errorState.observe(viewLifecycleOwner, Observer { uiException ->
           uiException.let {
                handleError(it)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username?.let { vmForgotPassword.changePassword(username!!) }

        changePasswordButton.setOnClickListener {
            vmForgotPassword.newPassword(newPasswordCode.text.toString(), newPassword.text.toString(), confirmNewPassword.text.toString())
        }
    }

//    override fun onDestroy() {
//        (activity?.application as DrawingApplication).releaseForgotPasswordComponent()
//        super.onDestroy()
//    }
//endregion

    //region view state and error handlers

    private fun handleViewState(forgotPasswordViewState: ForgotPasswordViewState) {
        when {
            forgotPasswordViewState.passwordChangeStarted.not() -> return
            forgotPasswordViewState.loading -> {
                showProgress(true)
            }
            forgotPasswordViewState.isPasswordChangeRequested -> {
                showProgress(true)
            }
            forgotPasswordViewState.isPasswordChanged -> {
                vmNavigation.goToMain()
            }
        }
    }

    private fun handleError(customUIException: UIException?) {
        showProgress(false)
        resetErrors()
        if (customUIException?.errorCode!! > 0) {
            val code = customUIException.errorCode
            if (code and UIException.EMPTY_CODE == UIException.EMPTY_CODE)
                showCodeError(getString(R.string.code_missing_error_message))
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
            changePasswordProgress.visibility = View.VISIBLE
        else
            changePasswordProgress.visibility = View.GONE
    }

    private fun showCodeError(message: String?) {
        newPasswordCodeLayout.error = message
    }

    private fun showPasswordError(message: String?) {
        newPasswordLayout.error = message
    }

    private fun showConfirmPasswordError(message: String?) {
        confirmNewPasswordLayout.error = message
    }

    private fun resetErrors() {
        newPasswordCodeLayout.error = null
        newPasswordLayout.error = null
        confirmNewPasswordLayout.error = null
    }
    //endregion

    companion object {
        private const val USERNAME = "username"

        @JvmStatic
        fun newInstance(username: String) =
                NewPasswordFragment().apply {
                    arguments = Bundle().apply {
                        putString(USERNAME, username)
                    }
                }
    }
}
