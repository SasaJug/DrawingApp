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
import com.sasaj.graphics.drawingapp.authentication.states.RegisterViewState
import com.sasaj.graphics.drawingapp.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : Fragment() {


    private lateinit var vmRegister: RegisterViewModel
    private lateinit var vmNavigation: AuthenticationNavigationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vmRegister = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        activity?.let {
            vmNavigation = ViewModelProviders.of(it).get(AuthenticationNavigationViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vmRegister.registerLiveData.observe(this, Observer { registerViewState -> handleViewState(registerViewState!!) })
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
                    && password?.text != null && password.text.toString() != "") {
                vmNavigation.loadingData()
                vmRegister.register(username?.text.toString(), password?.text.toString(), email?.text.toString())
            } else
                vmNavigation.error(RuntimeException("Username, email and password must not be empty!"))
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

    companion object {
        private val TAG = RegisterFragment::class.java.simpleName
    }
}
