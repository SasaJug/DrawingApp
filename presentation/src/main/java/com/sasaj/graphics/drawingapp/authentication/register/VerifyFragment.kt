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
import kotlinx.android.synthetic.main.fragment_verify.*
import javax.inject.Inject

class VerifyFragment : Fragment() {

    @Inject
    lateinit var verifyVMFactory: VerifyVMFactory

    private lateinit var vmVerify: VerifyViewModel
    private lateinit var vmNavigation: AuthenticationNavigationViewModel

    //region lifecycle callbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as DrawingApplication).createVerifyComponent().inject(this)

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
        vmVerify.errorState.observe(this, Observer { uiException ->
            uiException?.let {
                handleError(it)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_verify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyButton?.setOnClickListener {
            vmVerify.verify(verifyUsername!!.text.toString(), verifyCode!!.text.toString())
        }
    }

    override fun onDestroy() {
        (activity?.application as DrawingApplication).releaseVerifyComponent()
        super.onDestroy()
    }
    //endregion

    //region view state and error handlers
    private fun handleViewState(verifyViewState: VerifyViewState) {
        when {
            verifyViewState.verificationStarted.not() -> return
            verifyViewState.loading -> showProgress(true)
            verifyViewState.isVerified -> {
                showProgress(false)
                Log.i(TAG, "Verification Confirmed")
                vmNavigation.goToMain()
            }
        }
    }

    private fun handleError(customUIException: UIException?) {
        showProgress(false)
        resetErrors()
        if (customUIException?.errorCode!! > 0) {
            val code = customUIException.errorCode
            if (code and UIException.EMPTY_USERNAME == UIException.EMPTY_USERNAME)
                showUsernameError(getString(R.string.username_missing_error_message))
            if (code and (UIException.EMPTY_CODE) == UIException.EMPTY_CODE)
                showCodeError(getString(R.string.code_missing_error_message))
        } else {
            vmNavigation.error(customUIException)
        }
    }
    //endregion

    //region rendering
    private fun showProgress(show: Boolean) {
        if (show)
            verifyProgress.visibility = View.VISIBLE
        else
            verifyProgress.visibility = View.GONE
    }

    private fun showUsernameError(message: String?) {
        verifyUsernameLayout.error = message
    }

    private fun showCodeError(message: String?) {
        verifyCodeLayout.error = message
    }

    private fun resetErrors() {
        showUsernameError(null)
        showCodeError(null)
    }
    //endregion

    companion object {
        private val TAG = VerifyFragment::class.java.simpleName
    }
}
