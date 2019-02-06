package com.sasaj.data.repositories

import com.amazonaws.mobileconnectors.cognitoidentityprovider.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.sasaj.data.aws.AWSHelper
import com.sasaj.domain.UserRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject


class UserRepositoryImpl(private val awsHelper: AWSHelper, private val localRepository: LocalRepository) : UserRepository {

    companion object {
        val TAG: String = UserRepositoryImpl::class.java.simpleName
    }

    private lateinit var loginSubject: ReplaySubject<Boolean>
    private lateinit var checkLoggedInSubject: PublishSubject<String>
    private lateinit var registerSubject: PublishSubject<Boolean>
    private lateinit var verifySubject: PublishSubject<Boolean>
    private lateinit var changePasswordSubject: PublishSubject<Boolean>

    private var password: String? = null
    var forgotPasswordContinuation: ForgotPasswordContinuation? = null


    private var loginHandler: AuthenticationHandler? = object : AuthenticationHandler {
        override fun onSuccess(userSession: CognitoUserSession, newDevice: CognitoDevice?) {
            loginSubject.onNext(true)
        }

        override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation, userId: String?) {
            if (password != null) {
                val authenticationDetails = AuthenticationDetails(userId, password, null)
                password = null
                authenticationContinuation.setAuthenticationDetails(authenticationDetails)
                authenticationContinuation.continueTask()
            }
        }

        override fun getMFACode(continuation: MultiFactorAuthenticationContinuation) {
        }

        override fun authenticationChallenge(continuation: ChallengeContinuation) {
        }

        override fun onFailure(exception: Exception) {
            loginSubject.onError(exception)
        }
    }

    private var checkHandler: AuthenticationHandler? = object : AuthenticationHandler {
        override fun onSuccess(userSession: CognitoUserSession, newDevice: CognitoDevice?) {
            checkLoggedInSubject.onNext(userSession.username)
        }

        override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation, userId: String?) {
            checkLoggedInSubject.onNext("")
        }

        override fun getMFACode(continuation: MultiFactorAuthenticationContinuation) {
        }

        override fun authenticationChallenge(continuation: ChallengeContinuation) {
        }

        override fun onFailure(exception: Exception) {
            checkLoggedInSubject.onError(exception)
        }
    }

    private val signUpHandler = object : SignUpHandler {
        override fun onSuccess(user: CognitoUser, signUpConfirmationState: Boolean, cognitoUserCodeDeliveryDetails: CognitoUserCodeDeliveryDetails) {
            registerSubject.onNext(signUpConfirmationState)
        }

        override fun onFailure(exception: Exception) {
            registerSubject.onError(exception)
        }
    }

    private val genericHandler = object : GenericHandler {
        override fun onSuccess() {
            verifySubject.onNext(true)
        }

        override fun onFailure(exception: Exception) {
            verifySubject.onError(exception)
        }
    }

    private val forgotPasswordHandler: ForgotPasswordHandler = object : ForgotPasswordHandler {
        override fun onSuccess() {
            changePasswordSubject.onNext(true)
        }

        override fun getResetCode(continuation: ForgotPasswordContinuation?) {
            forgotPasswordContinuation = continuation
        }

        override fun onFailure(exception: Exception?) {
            changePasswordSubject.onError(exception as Throwable)
        }
    }

    override fun checkIfLoggedIn(): Observable<String> {
        checkLoggedInSubject = PublishSubject.create<String>()
        val user = awsHelper.getUserPool().currentUser
        user.getSessionInBackground(checkHandler)

        return checkLoggedInSubject
    }

    override fun logIn(username: String?, password: String?): Observable<Boolean> {
        loginSubject = ReplaySubject.create<Boolean>()
        this.password = password
        val user = awsHelper.getUserPool().getUser(username)
        if (user.userId != null) {
            user.getSessionInBackground(loginHandler)
        }
        return loginSubject
    }

    override fun signUp(username: String?, password: String?, attr: HashMap<String, String>): Observable<Boolean> {
        registerSubject = PublishSubject.create<Boolean>()
        val attributes = CognitoUserAttributes()
        attr.forEach { (key, value) -> attributes.addAttribute(key, value) }

        awsHelper.getUserPool().signUpInBackground(username,
                password,
                attributes,
                null,
                signUpHandler)

        return registerSubject
    }


    override fun signOut(): Observable<Boolean> {
        return Observable.fromCallable {
            localRepository.deleteAll()
            val username = awsHelper.getUserPool().currentUser.userId
            val user = awsHelper.getUserPool().getUser(username)
            user.signOut()
            true
        }
    }


    override fun verify(username: String?, code: String?): Observable<Boolean> {
        verifySubject = PublishSubject.create<Boolean>()
        val user = awsHelper.getUserPool().getUser(username)
        user?.confirmSignUpInBackground(code, false, genericHandler)

        return verifySubject
    }

    override fun changePassword(username: String?): Observable<Boolean> {
        changePasswordSubject = PublishSubject.create<Boolean>()
        val user = awsHelper.getUserPool().getUser(username)
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