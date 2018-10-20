package com.sasaj.graphics.drawingapp.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.cognito.CognitoHelper
import com.sasaj.graphics.drawingapp.ui.base.BaseActivity
import com.sasaj.graphics.drawingapp.ui.main.MainActivity

class LoginActivity : BaseActivity() {
    private var username: AutoCompleteTextView? = null
    private var password: EditText? = null
    private var login: Button? = null
    private var goToRegister: TextView? = null


    private var authenticationHandler: AuthenticationHandler = object : AuthenticationHandler {
        override fun onSuccess(userSession: CognitoUserSession, newDevice: CognitoDevice?) {
            Log.e(TAG, "Login success: ")
            if (userSession.isValid && userSession.idToken.jwtToken.isNotEmpty()) {
                Log.i(TAG, "onSuccess: " + userSession.idToken.jwtToken)
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.e(TAG, "onSuccess: no session data")
            }

        }

        override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation, userId: String) {
            val authenticationDetails = AuthenticationDetails(userId, password!!.text.toString(), null)
            authenticationContinuation.setAuthenticationDetails(authenticationDetails)
            authenticationContinuation.continueTask()
        }

        override fun getMFACode(continuation: MultiFactorAuthenticationContinuation) {
            Log.i(TAG, "getMFACode: ")
        }

        override fun authenticationChallenge(continuation: ChallengeContinuation) {
            Log.i(TAG, "authenticationChallenge: ")
        }

        override fun onFailure(exception: Exception) {
            Log.e(TAG, "Login failure: ", exception)
            showDialogMessage(exception.toString(), exception.message!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.sign_in_button)
        goToRegister = findViewById(R.id.go_to_register)

        login!!.setOnClickListener {
            val cognitoHelper = CognitoHelper(this@LoginActivity)
            val user = cognitoHelper.userPool.getUser(username!!.text.toString())
            user.getSessionInBackground(authenticationHandler)
        }

        goToRegister!!.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {

        private val TAG = LoginActivity::class.java.simpleName
    }
}
