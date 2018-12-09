package com.sasaj.graphics.drawingapp.authentication

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.BaseActivity
import com.sasaj.graphics.drawingapp.ui.main.MainActivity
import com.sasaj.graphics.drawingapp.viewmodel.VerifyViewModel
import com.sasaj.graphics.drawingapp.viewmodel.common.Response
import com.sasaj.graphics.drawingapp.viewmodel.common.Status
import kotlinx.android.synthetic.main.activity_verify.*

class VerifyActivity : BaseActivity() {


    private lateinit var vm: VerifyViewModel

    private val genericHandler = object : GenericHandler {
        override fun onSuccess() {
            Log.i(TAG, "Verification success!")
            val intent = Intent(this@VerifyActivity, MainActivity::class.java)
            startActivity(intent)
        }

        override fun onFailure(exception: Exception) {
            Log.e(TAG, "Verification Failure", exception)
            showDialogMessage(exception.toString(), exception.message!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)


        vm = ViewModelProviders.of(this).get(VerifyViewModel::class.java)
        vm.getVerifyLiveData().observe(this, Observer { response -> processResponse(response) })

        verifyButton?.setOnClickListener {
            vm.verify(verifyUsername!!.text.toString(), verificationCode!!.text.toString())
        }
    }
    private fun processResponse(response: Response?) {
        when (response?.status) {
            Status.LOADING -> renderLoadingState()
            Status.SUCCESS -> renderSucessRegisteringState(response.data)
            Status.ERROR -> renderErrorState(response.error)
        }
    }

    override fun resetViewModel() {
        vm.resetLiveData()
    }

    private fun renderLoadingState() {
        showProgress("wait...")
    }

    private fun renderSucessRegisteringState(status: String?) {
        hideProgress()
        if (status == "verified") {
            Log.i(TAG, "Succesfully verified")
            val intent = Intent(this@VerifyActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.i(TAG, "Verify unsuccessful")
            showDialogMessage("User not verified", "Try again")
            finish()
        }
    }

    private fun renderErrorState(throwable: Throwable?) {
        hideProgress()
        Log.e(TAG, "Error verify ", throwable)
        showDialogMessage("Error verifying email. ", throwable.toString())
    }

    companion object {
        private val TAG = VerifyActivity::class.java.simpleName
    }
}
