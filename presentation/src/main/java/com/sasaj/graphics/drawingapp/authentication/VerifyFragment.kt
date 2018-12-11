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
import com.sasaj.graphics.drawingapp.authentication.states.VerifyViewState
import com.sasaj.graphics.drawingapp.viewmodel.VerifyViewModel
import kotlinx.android.synthetic.main.fragment_verify.*

class VerifyFragment : Fragment() {

    private lateinit var vmVerify: VerifyViewModel
    private lateinit var vmNavigation: AuthenticationNavigationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vmVerify = ViewModelProviders.of(this).get(VerifyViewModel::class.java)
        activity?.let {
            vmNavigation = ViewModelProviders.of(it).get(AuthenticationNavigationViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vmVerify.verifyLiveData.observe(this, Observer {
            if (it != null) handleViewState(it)
        })
        vmVerify.errorState.observe(this, Observer { throwable ->
            throwable?.let {
                vmNavigation.error(it)
            }
        })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyButton?.setOnClickListener {
            vmVerify.verify(verifyUsername!!.text.toString(), verificationCode!!.text.toString())
        }
    }


    private fun handleViewState(verifyViewState: VerifyViewState) {
        when {
            verifyViewState.verificationStarted.not() -> return
            verifyViewState.loading -> vmNavigation.loadingData()
            verifyViewState.isVerified -> {
                Log.i(TAG, "Verification Confirmed")
                vmNavigation.verifyConfirmed()
            }
        }
    }

    companion object {
        private val TAG = VerifyFragment::class.java.simpleName
    }
}
