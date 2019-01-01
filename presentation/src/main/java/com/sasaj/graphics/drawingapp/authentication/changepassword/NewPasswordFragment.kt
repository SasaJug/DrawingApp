package com.sasaj.graphics.drawingapp.authentication.changepassword

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sasaj.graphics.drawingapp.DrawingApplication
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.authentication.AuthenticationNavigationViewModel
import com.sasaj.graphics.drawingapp.common.UIException
import kotlinx.android.synthetic.main.fragment_new_password.*
import javax.inject.Inject


class NewPasswordFragment : Fragment() {

    @Inject
    lateinit var forgotPasswordVMFactory: ForgotPasswordVMFactory

    private var username: String? = null

    private lateinit var vmForgotPassword: ForgotPasswordViewModel
    private lateinit var vmNavigation: AuthenticationNavigationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as DrawingApplication).createForgotPasswordComponent().inject(this)

        arguments?.let {
            username = it.getString(USERNAME)
        }

        vmForgotPassword = ViewModelProviders.of(this, forgotPasswordVMFactory).get(ForgotPasswordViewModel::class.java)
        activity?.let {
            vmNavigation = ViewModelProviders.of(it).get(AuthenticationNavigationViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vmForgotPassword.forgotPasswordLiveData.observe(this, Observer {
            if (it != null) handleViewState(it)
        })
        vmForgotPassword.errorState.observe(this, Observer { throwable ->
            throwable?.let {
                vmNavigation.error(it)
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
            if (newPassword.text.toString().equals(confirmPassword.text.toString())) {
                vmForgotPassword.newPassword(newPassword.text.toString(), newPasswordCode.text.toString())
            } else {
                vmNavigation.error(UIException("Passwords do not match", IllegalArgumentException()))
            }
        }
    }


    private fun handleViewState(forgotPasswordViewState: ForgotPasswordViewState) {
        when {
            forgotPasswordViewState.passwordChangeStarted.not() -> return
            forgotPasswordViewState.loading -> {
                vmNavigation.loadingData(); return
            }
            forgotPasswordViewState.isPasswordChangeRequested -> {
                vmNavigation.passwordChangeRequested(); return
            }
            forgotPasswordViewState.isPasswordChanged -> {
                vmNavigation.passwordChangeSuccessful(); return
            }
        }
    }

    override fun onDestroy() {
        (activity?.application as DrawingApplication).releaseForgotPasswordComponent()
        super.onDestroy()
    }


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
