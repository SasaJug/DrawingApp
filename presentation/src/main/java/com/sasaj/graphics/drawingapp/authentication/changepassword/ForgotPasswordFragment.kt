package com.sasaj.graphics.drawingapp.authentication.changepassword


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.authentication.AuthenticationNavigationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_forgot_pasword.*

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private val vmNavigation by activityViewModels<AuthenticationNavigationViewModel>()

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
}
