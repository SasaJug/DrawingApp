package com.sasaj.data.repositories

import android.util.Log
import com.amazonaws.mobileconnectors.cognitoidentityprovider.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.sasaj.data.remote.AWSHelper
import com.sasaj.domain.UserRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


class UserRepositoryImpl(private val AWSHelper: AWSHelper, private val localRepository: LocalRepository) : UserRepository {

    companion object {
        val TAG: String = UserRepositoryImpl::class.java.simpleName
    }

    private lateinit var loginSubject: PublishSubject<String>
    private lateinit var checkLoggedInSubject: PublishSubject<String>
    private lateinit var registerSubject: PublishSubject<Boolean>
    private lateinit var verifySubject: PublishSubject<Boolean>
    private lateinit var changePasswordSubject: PublishSubject<Boolean>

    private var password: String? = null
    var forgotPasswordContinuation: ForgotPasswordContinuation? = null


    private var loginHandler: AuthenticationHandler? = object : AuthenticationHandler {
        override fun onSuccess(userSession: CognitoUserSession, newDevice: CognitoDevice?) {
            Log.i(TAG, "Login success: ")
            loginSubject.onNext(userSession.username)
        }

        override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation, userId: String?) {
            Log.i(TAG, "getAuthenticationDetails")
            if (password != null) {
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
            loginSubject.onError(exception)
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

    private val genericHandler = object : GenericHandler {
        override fun onSuccess() {
            Log.i(TAG, "Verification success!")
            verifySubject.onNext(true)
        }

        override fun onFailure(exception: Exception) {
            verifySubject.onError(exception)
        }
    }

    val forgotPasswordHandler: ForgotPasswordHandler = object : ForgotPasswordHandler {
        override fun onSuccess() {
            changePasswordSubject.onNext(true)
        }

        override fun getResetCode(continuation: ForgotPasswordContinuation?) {
            forgotPasswordContinuation = continuation
        }

        override fun onFailure(exception: Exception?) {
            changePasswordSubject.onError(exception as Throwable)
//            newPasswordSubject.onError(exception as Throwable)
        }
    }

    override fun checkIfLoggedIn(): Observable<String> {
        checkLoggedInSubject = PublishSubject.create<String>()
        val user = AWSHelper?.userPool?.currentUser
        val username = user?.userId
        if (username != null) {
            user.getSessionInBackground(checkHandler)
        } else {
            checkLoggedInSubject.onNext("")
        }
        return checkLoggedInSubject
    }

    override fun logIn(username: String?, password: String?): Observable<String> {
        loginSubject = PublishSubject.create<String>()
        this.password = password
        val user = AWSHelper?.userPool?.getUser(username)
        if (user?.userId != null) {
            user.getSessionInBackground(loginHandler)
        }
        return loginSubject
    }

    override fun signUp(username: String?, password: String?, attr: HashMap<String, String>): Observable<Boolean> {
        registerSubject = PublishSubject.create<Boolean>()
        val attributes = CognitoUserAttributes()
        attr.forEach { (key, value) -> attributes.addAttribute(key, value) }

        AWSHelper.userPool.signUpInBackground(username,
                password,
                attributes,
                null,
                signUpHandler)

        return registerSubject
    }


    override fun signOut(): Observable<Boolean> {
        return Observable.fromCallable {
            localRepository.deleteAll()
            val username = AWSHelper.userPool.currentUser.userId
            val user = AWSHelper.userPool.getUser(username)
            user.signOut()
            true
        }
    }


    override fun verify(username: String?, code: String?): Observable<Boolean> {
        verifySubject = PublishSubject.create<Boolean>()
        val user = AWSHelper.userPool.getUser(username)
        user?.confirmSignUpInBackground(code, false, genericHandler)

        return verifySubject
    }

    override fun changePassword(username: String?): Observable<Boolean> {
        changePasswordSubject = PublishSubject.create<Boolean>()
//        newPasswordSubject = PublishSubject.create<Boolean>()
        val user = AWSHelper.userPool.getUser(username)
        user.forgotPasswordInBackground(forgotPasswordHandler)
        changePasswordSubject.onNext(true)

        return changePasswordSubject
    }

    override fun newPassword(newPassword: String?, code: String?): Observable<Boolean> {

        forgotPasswordContinuation?.setVerificationCode(code)
        forgotPasswordContinuation?.setPassword(newPassword)
        forgotPasswordContinuation?.continueTask()

        return changePasswordSubject
    }

}