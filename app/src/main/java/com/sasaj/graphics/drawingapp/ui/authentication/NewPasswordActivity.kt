package com.sasaj.graphics.drawingapp.ui.authentication

import android.content.Intent
import android.os.Bundle
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.cognito.CognitoHelper
import com.sasaj.graphics.drawingapp.ui.base.BaseActivity
import com.sasaj.graphics.drawingapp.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_new_password.*
import java.lang.Exception

class NewPasswordActivity : BaseActivity() {

    var forgotPasswordContinuation:  ForgotPasswordContinuation? = null

    val forgotPasswordHandler: ForgotPasswordHandler = object: ForgotPasswordHandler{
        override fun onSuccess() {
            val intent = Intent(this@NewPasswordActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        override fun getResetCode(continuation: ForgotPasswordContinuation?) {
            forgotPasswordContinuation = continuation
        }

        override fun onFailure(exception: Exception?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)

        val cognitoHelper = CognitoHelper(this@NewPasswordActivity)
        val user = cognitoHelper.userPool.getUser(intent.getStringExtra("username"))
        user.forgotPasswordInBackground(forgotPasswordHandler)

        changePasswordButton.setOnClickListener {
            if(newPassword.text.toString().equals(confirmPassword.text.toString())){
                forgotPasswordContinuation?.setVerificationCode(newPasswordCode.text.toString())
                forgotPasswordContinuation?.setPassword(newPassword.text.toString())
                forgotPasswordContinuation?.continueTask()
            } else {
                showDialogMessage("Error", "Passwords do not match!")
            }

        }
    }
}
