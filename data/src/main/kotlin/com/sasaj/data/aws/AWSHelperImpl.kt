package com.sasaj.data.aws

import android.content.Context
import android.os.Environment
import android.util.Log
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.regions.Regions
import com.sasaj.data.BuildConfig
import com.sasaj.data.repositories.RemoteDrawingRepository
import java.io.File

internal class AWSHelperImpl(private val context: Context): AWSHelper {

    private val userPoolId = BuildConfig.USER_POOL_ID
    private val clientId = BuildConfig.CLIENT_ID
    private val clientSecret = BuildConfig.CLIENT_SECRET

    private val identityPoolRegion = Regions.fromName(BuildConfig.IDENTITY_POOL_REGION)
    private val identityPoolId = BuildConfig.IDENTITY_POOL_ID

    override fun getCredentialsProvider(): CognitoCachingCredentialsProvider {
        return CognitoCachingCredentialsProvider(
                context,
                identityPoolId,
                identityPoolRegion)
    }

    override fun getUserPool(): CognitoUserPool {
        return CognitoUserPool(context, userPoolId, clientId, clientSecret, identityPoolRegion)
    }

    override fun getS3BucketName(): String {
        return BuildConfig.s3_BUCKET_NAME
    }

    override fun getS3BucketRegion(): String {
        return BuildConfig.s3_BUCKET_REGION
    }

    override fun getStorageDirectory(): File  {
        val root: File =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val storageDirectory = File(root, "DrawingApp/" + getUserPool().currentUser.userId)

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
