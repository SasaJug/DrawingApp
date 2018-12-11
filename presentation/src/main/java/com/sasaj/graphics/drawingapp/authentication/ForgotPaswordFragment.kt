package com.sasaj.graphics.drawingapp.authentication


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sasaj.graphics.drawingapp.R
import kotlinx.android.synthetic.main.fragment_forgot_pasword.*

class ForgotPaswordFragment : Fragment() {

    private lateinit var vmNavigation: AuthenticationNavigationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            vmNavigation = ViewModelProviders.of(it).get(AuthenticationNavigationViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_pasword, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCodeButton?.setOnClickListener {
            vmNavigation.getCode(forgotPasswordUsername.text.toString())
        }
    }

}
