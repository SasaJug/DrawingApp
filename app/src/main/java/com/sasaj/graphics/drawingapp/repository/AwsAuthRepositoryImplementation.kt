package com.sasaj.graphics.drawingapp.repository

import android.content.Context
import android.util.Log
import com.amazonaws.mobileconnectors.cognitoidentityprovider.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.sasaj.graphics.drawingapp.cognito.CognitoHelper
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.AuthRepository
import io.reactivex.subjects.PublishSubject

class AwsAuthRepositoryImplementation(val context: Context) : AuthRepository {

    companion object {
        val TAG: String = AwsAuthRepositoryImplementation::class.java.simpleName
    }

    private var cognitoHelper: CognitoHelper? = null

    private var loginSubject: PublishSubject<String> = PublishSubject.create<String>()
    private var checkLoggedInSubject: PublishSubject<String> = PublishSubject.create<String>()
    private var registerSubject: PublishSubject<Boolean> = PublishSubject.create<Boolean>()

    // ToDo - secure
    private var password: String? = null

    private var loginHandler: AuthenticationHandler? = object : AuthenticationHandler {
        override fun onSuccess(userSession: CognitoUserSession, newDevice: CognitoDevice?) {
            Log.i(TAG, "Login success: ")
            loginSubject.onNext("success")
        }

        override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation, userId: String?) {
            Log.i(TAG, "getAuthenticationDetails")
            if(password != null){
                val authenticationDetails = AuthenticationDetails(userId, password, null)
                password = null
                authenticationContinuation.setAuthenticationDetails(authenticationDetails)
                authenticationContinuation.continueTask()
            }
        }

        override fun getMFACode(continuation: MultiFactorAuthenticationContinuation) {
            Log.i(TAG, "getMFACode")
        }

        override fun authenticationChallenge(continuation: ChallengeContinuation) {
            Log.i(TAG, "gauthenticationChallenge")
        }

        override fun onFailure(exception: Exception) {
            Log.e(TAG, "Login failure: ", exception)
            loginSubject.onNext("error")
        }
    }

    private var checkHandler: AuthenticationHandler? = object : AuthenticationHandler {
        override fun onSuccess(userSession: CognitoUserSession, newDevice: CognitoDevice?) {
            Log.i(TAG, "Check logged in success: ")
            checkLoggedInSubject.onNext(userSession.username)
        }

        override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation, userId: String?) {
            Log.i(TAG, "getAuthenticationDetails")
            checkLoggedInSubject.onNext("")
        }

        override fun getMFACode(continuation: MultiFactorAuthenticationContinuation) {
            Log.i(TAG, "getMFACode")
        }

        override fun authenticationChallenge(continuation: ChallengeContinuation) {
            Log.i(TAG, "getAuthenticationChallenge")
        }

        override fun onFailure(exception: Exception) {
            Log.e(TAG, "Login failure: ${exception.message}")
            checkLoggedInSubject.onError(exception)
        }
    }

    private val signUpHandler = object : SignUpHandler {
        override fun onSuccess(user: CognitoUser, signUpConfirmationState: Boolean, cognitoUserCodeDeliveryDetails: CognitoUserCodeDeliveryDetails) {
            registerSubject.onNext(signUpConfirmationState)
         }

        override fun onFailure(exception: Exception) {
            Log.e(TAG, "Signup Failure: ", exception)
            registerSubject.onError(exception)
        }
    }


    init {
        cognitoHelper = CognitoHelper(context)
    }

    override fun checkIfLoggedIn() {
        val user = cognitoHelper?.userPool?.currentUser
        val username = user?.userId
        if (username != null) {
            user.getSessionInBackground(checkHandler)
        } else {
            checkLoggedInSubject.onNext("")
        }
    }

    override fun logIn(username : String?, password : String?){
        this.password = password
        val user = cognitoHelper?.userPool?.getUser(username)
        if (user?.userId != null) {
            user.getSessionInBackground(loginHandler)
        }
    }

    override fun signUp(username : String?, password : String?, attr : HashMap<String, String>){
        val attributes = CognitoUserAttributes()
        attr.forEach { (key, value) -> attributes.addAttribute(key,value)}

        cognitoHelper?.userPool?.signUpInBackground(username,
                password,
                attributes,
                null,
                signUpHandler)
    }

    override fun getCheckLoggedInSubject(): PublishSubject<String> {
        return checkLoggedInSubject
    }

    override fun getLoginSubject(): PublishSubject<String> {
        return loginSubject
    }

    override fun getRegisterSubject(): PublishSubject<Boolean> {
        return registerSubject
    }



}