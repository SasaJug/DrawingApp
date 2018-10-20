package com.sasaj.graphics.drawingapp.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.sasaj.graphics.drawingapp.cognito.CognitoHelper
import com.sasaj.graphics.drawingapp.ui.authentication.LoginActivity
import com.sasaj.graphics.drawingapp.ui.base.BaseActivity
import com.sasaj.graphics.drawingapp.ui.main.MainActivity

/**
 * Created by sjugurdzija on 4/22/2017
 */

class SplashActivity : BaseActivity() {

    private var username : String? = null
    val cognitoHelper : CognitoHelper  =  CognitoHelper(this)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_STORAGE)

            } else {
                findCurrent()
            }
        } else {
            findCurrent()
        }
    }


    private fun findCurrent() {

        val user = cognitoHelper.userPool.currentUser
        username = user.userId
        if (username != null) {
            user.getSessionInBackground(authenticationHandler)
        } else  {
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    findCurrent()
                } else {
                    finish()
                }
                return
            }
        }
    }

    private var authenticationHandler: AuthenticationHandler = object : AuthenticationHandler {
        override fun onSuccess(userSession: CognitoUserSession, newDevice: CognitoDevice?) {
            Log.e(TAG, "Login success: ")
            if (userSession.isValid && userSession.idToken.jwtToken.isNotEmpty()) {
                Log.e(TAG, "onSuccess: " + userSession.idToken.jwtToken)
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.e(TAG, "onSuccess: no session data")
            }
        }

        override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation, userId: String) {
            Log.e(TAG, "getAuthenticationDetails")
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        override fun getMFACode(continuation: MultiFactorAuthenticationContinuation) {
            Log.e(TAG, "getMFACode")
        }

        override fun authenticationChallenge(continuation: ChallengeContinuation) {
            Log.e(TAG, "gauthenticationChallenge")
        }

        override fun onFailure(exception: Exception) {
            Log.i(TAG, "Login failure: ", exception)
        }
    }

    companion object {
        private val TAG = SplashActivity::class.java.simpleName
        private const val MY_PERMISSIONS_REQUEST_STORAGE = 10
    }

}
