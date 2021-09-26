package com.sasaj.graphics.drawingapp.authentication.register


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.sasaj.domain.usecases.VerifyUser
import com.sasaj.graphics.drawingapp.DrawingApplication
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.authentication.AuthenticationNavigationViewModel
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import com.sasaj.graphics.drawingapp.common.UIException
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.synthetic.main.fragment_verify.*
import javax.inject.Inject


data class VerifyViewState(
    var verificationStarted : Boolean  = false,
    var loading : Boolean = false,
    var isVerified: Boolean = false
)

@AndroidEntryPoint
class VerifyFragment : androidx.fragment.app.Fragment() {

    private val vmVerify by viewModels<VerifyViewModel>()
    private val vmNavigation by activityViewModels<AuthenticationNavigationViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vmVerify.verifyLiveData.observe(viewLifecycleOwner, Observer {
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

    companion object {
        private val TAG = VerifyFragment::class.java.simpleName
    }
}

@HiltViewModel
class VerifyViewModel @Inject constructor(private val verifyUserUseCase: VerifyUser) : BaseViewModel() {

    val verifyLiveData: MutableLiveData<VerifyViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<UIException> = SingleLiveEvent()

    init {
        verifyLiveData.value = VerifyViewState()
    }

    fun verify(username: String, code: String) {
        var errorCode = 0

        if (username.trim() == "")
            errorCode = errorCode or UIException.EMPTY_USERNAME
        if (code.trim() == "")
            errorCode = errorCode or UIException.EMPTY_CODE

        if (errorCode > 0) {
            errorState.value = UIException("All entries must be valid", IllegalArgumentException(), errorCode)
            return
        }

        val verifyViewState = verifyLiveData.value?.copy(verificationStarted = true, loading = true, isVerified = false)
        verifyLiveData.value = verifyViewState

        addDisposable(verifyUserUseCase.verifyUser(username, code)
            .subscribe(
                { b: Boolean ->
                    val newVerifyViewState = verifyLiveData.value?.copy(verificationStarted = true, loading = false, isVerified = b)
                    verifyLiveData.value = newVerifyViewState
                    errorState.value = null
                },
                { e -> errorState.value = UIException(cause = e) },
                { Log.i(TAG, "Verification completed") }
            )
        )
    }

    companion object {
        private val TAG = VerifyViewModel::class.java.simpleName
    }

}
