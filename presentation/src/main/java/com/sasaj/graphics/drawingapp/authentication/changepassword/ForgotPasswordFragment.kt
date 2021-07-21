package com.sasaj.graphics.drawingapp.authentication.changepassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.authentication.AuthenticationNavigationViewModel
import kotlinx.android.synthetic.main.fragment_forgot_pasword.*

class ForgotPasswordFragment : Fragment() {

    private lateinit var vmNavigation: AuthenticationNavigationViewModel

    //region lifecycle callbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            vmNavigation = ViewModelProviders.of(it).get(AuthenticationNavigationViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_forgot_pasword, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCodeButton?.setOnClickListener {
            vmNavigation.getCode(forgotPasswordUsername.text.toString())
        }
    }
    //endregion
}
