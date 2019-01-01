package com.sasaj.graphics.drawingapp.authentication.register

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
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


class RegisterFragment : Fragment() {

    @Inject
    lateinit var registerVMFactory: RegisterVMFactory

    private lateinit var vmRegister: RegisterViewModel
    private lateinit var vmNavigation: AuthenticationNavigationViewModel

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
        vmRegister.registerLiveData.observe(this, Observer {
            if (it != null) handleViewState(it)
        })
        vmRegister.errorState.observe(this, Observer { throwable ->
            throwable?.let {
                vmNavigation.error(it)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpButton!!.setOnClickListener {
            if (username?.text != null && username.text.toString() != ""
                    && email?.text != null && email.text.toString() != ""
                    && password?.text != null && password.text.toString() != ""
                    && confirmPassword.text != null && confirmPassword.text.toString() != "") {
                if (password.text.toString() == confirmPassword.text.toString()) {
                    vmNavigation.loadingData()
                    vmRegister.register(username?.text.toString(), password?.text.toString(), email?.text.toString())
                } else {
                    vmNavigation.error(UIException(getString(R.string.passwords_do_not_match_error), IllegalArgumentException()))
                }
            } else
                vmNavigation.error(UIException(getString(R.string.fields_empty_error_message), IllegalArgumentException()))
        }
    }

    private fun handleViewState(registerViewState: RegisterViewState) {
        when {
            registerViewState.registrationStarted.not() -> return
            registerViewState.loading -> vmNavigation.loadingData()
            registerViewState.isConfirmed -> {
                Log.i(TAG, "Registration Confirmed")
                vmNavigation.registerConfirmed()
            }
            else -> {
                Log.i(TAG, "Registration NotConfirmed")
                vmNavigation.registerNotConfirmed()
            }
        }
    }

    override fun onDestroy() {
        (activity?.application as DrawingApplication).releaseRegisterComponent()
        super.onDestroy()
    }

    companion object {
        private val TAG = RegisterFragment::class.java.simpleName
    }
}
