package com.sasaj.graphics.drawingapp.aws

import android.content.Context

import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.regions.Regions
import com.sasaj.graphics.drawingapp.BuildConfig

class CognitoHelper(private val context: Context) {
    private val userPoolId = BuildConfig.USER_POOL_ID
    private val clientId = BuildConfig.CLIENT_ID
    private val clientSecret = BuildConfig.CLIENT_SECRET

    private val identityPoolRegion = Regions.US_EAST_2
    private val identityPoolId = BuildConfig.IDENTITY_POOL_ID

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
