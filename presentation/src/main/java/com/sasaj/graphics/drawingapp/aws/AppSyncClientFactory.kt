package com.sasaj.graphics.drawingapp.aws

import android.content.Context

import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import com.amazonaws.mobileconnectors.appsync.sigv4.BasicCognitoUserPoolsAuthProvider
import com.amazonaws.regions.Regions

class AppSyncClientFactory (context: Context, cognitoHelper: CognitoHelper){

    val configProvider = AWSConfiguration(context)
    val appSyncConfig = configProvider.optJsonObject("AppSync")
    val cognitoUserPool = cognitoHelper.userPool

    val client: AWSAppSyncClient = AWSAppSyncClient.builder()
            .context(context)
            .region(Regions.fromName(appSyncConfig.getString("region")))
            .cognitoUserPoolsAuthProvider(BasicCognitoUserPoolsAuthProvider(cognitoUserPool))
            .serverUrl(appSyncConfig.getString("graphqlEndpoint"))
            .build()
}
