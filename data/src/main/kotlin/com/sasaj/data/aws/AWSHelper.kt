package com.sasaj.data.aws

import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import java.io.File

interface AWSHelper {
    fun getCredentialsProvider(): CognitoCachingCredentialsProvider
    fun getUserPool(): CognitoUserPool
    fun getStorageDirectory(): File
    fun getS3BucketName(): String
    fun getS3BucketRegion(): String
}