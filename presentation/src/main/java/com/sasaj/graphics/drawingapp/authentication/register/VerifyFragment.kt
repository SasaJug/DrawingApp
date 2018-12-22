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
import kotlinx.android.synthetic.main.fragment_verify.*
import javax.inject.Inject

class VerifyFragment : Fragment() {

    @Inject
    lateinit var verifyVMFactory: VerifyVMFactory

    private lateinit var vmVerify: VerifyViewModel
    private lateinit var vmNavigation: AuthenticationNavigationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as DrawingApplication).createVerifyComponenet().inject(this)

        vmVerify = ViewModelProviders.of(this, verifyVMFactory).get(VerifyViewModel::class.java)
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

    override fun onDestroy() {
        (activity?.application as DrawingApplication).releaseVerifyComponent()
        super.onDestroy()
    }

    companion object {
        private val TAG = VerifyFragment::class.java.simpleName
    }
}
