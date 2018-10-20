package com.sasaj.graphics.drawingapp.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.cognito.CognitoHelper
import com.sasaj.graphics.drawingapp.ui.base.BaseActivity
import com.sasaj.graphics.drawingapp.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_verify.*

class VerifyActivity : BaseActivity() {

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

        verifyButton?.setOnClickListener {
            val cognitoHelper = CognitoHelper(this@VerifyActivity)
            val user = cognitoHelper.userPool.getUser(verifyUsername!!.text.toString())
            user.confirmSignUpInBackground(verificationCode!!.text.toString(), false, genericHandler)
        }
    }

    companion object {
        private val TAG = VerifyActivity::class.java.simpleName
    }
}
