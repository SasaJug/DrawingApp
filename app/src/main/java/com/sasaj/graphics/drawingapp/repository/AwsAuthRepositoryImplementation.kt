package com.sasaj.graphics.drawingapp.repository

import android.content.Context
import android.util.Log
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.sasaj.graphics.drawingapp.cognito.CognitoHelper
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

class AwsAuthRepositoryImplementation(val context: Context) {


    companion object {
        val TAG: String = AwsAuthRepositoryImplementation::class.java.simpleName
    }

    private var cognitoHelper: CognitoHelper? = null
    private var authSubject: Subject<String> = BehaviorSubject.create<String>()
    private var authenticationHandler: AuthenticationHandler? = object : AuthenticationHandler {
        override fun onSuccess(userSession: CognitoUserSession, newDevice: CognitoDevice?) {
            Log.i(TAG, "Login success: ")
            authSubject.onNext(userSession.username)
        }

        override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation, userId: String) {
            Log.i(TAG, "getAuthenticationDetails")
            authSubject.onNext("")
        }

        override fun getMFACode(continuation: MultiFactorAuthenticationContinuation) {
            Log.i(TAG, "getMFACode")
        }

        override fun authenticationChallenge(continuation: ChallengeContinuation) {
            Log.i(TAG, "gauthenticationChallenge")
        }

        override fun onFailure(exception: Exception) {
            Log.e(TAG, "Login failure: ", exception)
            authSubject.onError(exception)
        }
    }

    init {
        cognitoHelper = CognitoHelper(context)
    }

    fun isLoggedIn() {
        val user = cognitoHelper?.userPool?.currentUser
        val username = user?.userId
        if (username != null) {
            user.getSessionInBackground(authenticationHandler)
        }
    }


    fun getAuthenticationSubject(): Subject<String> {
        return authSubject
    }


}