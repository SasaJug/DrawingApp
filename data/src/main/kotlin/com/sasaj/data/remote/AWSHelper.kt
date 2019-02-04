package com.sasaj.data.remote

import android.content.Context
import android.os.Environment
import android.util.Log

import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.regions.Regions
import com.sasaj.data.BuildConfig
import com.sasaj.data.repositories.RemoteDrawingRepository
import java.io.File

class AWSHelper(private val context: Context) {
    private val userPoolId = BuildConfig.USER_POOL_ID
    private val clientId = BuildConfig.CLIENT_ID
    private val clientSecret = BuildConfig.CLIENT_SECRET

    private val identityPoolRegion = Regions.fromName(BuildConfig.IDENTITY_POOL_REGION)
    private val identityPoolId = BuildConfig.IDENTITY_POOL_ID

    val s3BucketName = BuildConfig.s3_BUCKET_NAME
    val s3BucketRegion =BuildConfig.s3_BUCKET_REGION

    // User details from the service
    var currSession : CognitoUserSession? = null
    var userDetails : CognitoUserDetails? = null
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

    fun storageDirectory(): File  {
        val root: File =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val storageDirectory = File(root, "DrawingApp/"+userPool.currentUser.userId)

        if (!storageDirectory.exists()) {
            try {
                storageDirectory.mkdirs()
            } catch (se: SecurityException) {
                Log.e(RemoteDrawingRepository.TAG, "")
            }
        }

        return storageDirectory
    }


}
