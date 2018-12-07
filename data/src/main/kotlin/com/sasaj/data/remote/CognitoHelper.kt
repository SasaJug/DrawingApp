package com.sasaj.data.remote

import android.content.Context

import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.regions.Regions

class CognitoHelper(private val context: Context) {
    private val userPoolId = "us-east-2_FG8teOoa8"
    private val clientId = "4456i56t5ta1stqjf52h7frpdh"
    private val clientSecret = "9973ct72mjh7e73954blmc0lcbm93toidea696m25v2upt4fmbu"

    private val identityPoolRegion = Regions.US_EAST_2
    private val identityPoolId = "us-east-2:90852a12-816d-425d-942d-3a62742bac51"

    // User details from the service
    var currSession: CognitoUserSession? = null
    var userDetails: CognitoUserDetails? = null
    var newDevice : CognitoDevice? = null

    val userPool: CognitoUserPool
        get() = CognitoUserPool(context, userPoolId, clientId, clientSecret, identityPoolRegion)

    // Initialize the Amazon Cognito credentials provider
    // Identity pool ID
    // Region
    val credentialsProvider: CognitoCachingCredentialsProvider
        get() = CognitoCachingCredentialsProvider(
                context,
                identityPoolId,
                identityPoolRegion
        )

}
