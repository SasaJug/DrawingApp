package com.sasaj.graphics.drawingapp.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.cognito.CognitoHelper
import com.sasaj.graphics.drawingapp.ui.base.BaseActivity
import com.sasaj.graphics.drawingapp.ui.main.MainActivity

class RegisterActivity : BaseActivity() {
    private var username: AutoCompleteTextView? = null
    private var email: AutoCompleteTextView? = null
    private var password: EditText? = null
    private var signupButton: Button? = null

    private val attributes = CognitoUserAttributes()

    private val signUpHandler = object : SignUpHandler {
        override fun onSuccess(user: CognitoUser, signUpConfirmationState: Boolean, cognitoUserCodeDeliveryDetails: CognitoUserCodeDeliveryDetails) {
            Log.i(TAG, "onSuccess - is confirmed: $signUpConfirmationState")
            if (!signUpConfirmationState) {
                Log.i(TAG, "onSuccess: not confirmed, token sent to: " + cognitoUserCodeDeliveryDetails.destination)
                val intent = Intent(this@RegisterActivity, VerifyActivity::class.java)
                startActivity(intent)
            } else {
                Log.i(TAG, "onSuccess - confirmed: ")
                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }

        override fun onFailure(exception: Exception) {
            Log.e(TAG, "Signup Failure: ", exception)
            showDialogMessage(exception.toString(), exception.message!!)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        username = findViewById(R.id.username)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        signupButton = findViewById(R.id.sign_up_button)

        signupButton!!.setOnClickListener {
            attributes.addAttribute("email", email!!.text.toString())

            val cognitoHelper = CognitoHelper(this@RegisterActivity)
            cognitoHelper.userPool.signUpInBackground(username!!.text.toString(),
                    password!!.text.toString(),
                    attributes,
                    null,
                    signUpHandler)
        }
    }

    companion object {

        private val TAG = RegisterActivity::class.java.simpleName
    }
}
