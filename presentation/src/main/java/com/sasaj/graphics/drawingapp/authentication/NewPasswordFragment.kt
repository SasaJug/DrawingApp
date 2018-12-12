package com.sasaj.graphics.drawingapp.authentication

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.authentication.states.PasswordChangeViewState
import com.sasaj.graphics.drawingapp.authentication.viewmodels.ChangePasswordViewModel
import kotlinx.android.synthetic.main.fragment_new_password.*


class NewPasswordFragment : Fragment() {
    private var username: String? = null

    private lateinit var vmChangePassword: ChangePasswordViewModel
    private lateinit var vmNavigation: AuthenticationNavigationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            username = it.getString(USERNAME)
        }

        vmChangePassword = ViewModelProviders.of(this).get(ChangePasswordViewModel::class.java)
        activity?.let {
            vmNavigation = ViewModelProviders.of(it).get(AuthenticationNavigationViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vmChangePassword.passwordChangeLiveData.observe(this, Observer {
            if (it != null) handleViewState(it)
        })
        vmChangePassword.errorState.observe(this, Observer { throwable ->
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

        username?.let { vmChangePassword.changePassword(username!!) }

        changePasswordButton.setOnClickListener {
            if (newPassword.text.toString().equals(confirmPassword.text.toString())) {
                vmChangePassword.newPassword(newPassword.text.toString(), newPasswordCode.text.toString())
            } else {
                vmNavigation.error(RuntimeException("Passwords do not match"))
            }
        }
    }


    private fun handleViewState(passwordChangeViewState: PasswordChangeViewState) {
        when {
            passwordChangeViewState.passwordChangeStarted.not() -> return
            passwordChangeViewState.loading -> {
                vmNavigation.loadingData(); return
            }
            passwordChangeViewState.isPasswordChangeRequested -> {
                vmNavigation.passwordChangeRequested(); return
            }
            passwordChangeViewState.isPasswordChanged -> {
                vmNavigation.passwordChangeSuccessful(); return
            }
        }
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
