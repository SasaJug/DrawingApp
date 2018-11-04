package com.sasaj.graphics.drawingapp.aws;

import android.content.Context;

import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.regions.Regions;

public class AppSyncClientFactory {
    private static volatile AWSAppSyncClient client;

    public synchronized static AWSAppSyncClient getInstance(Context context, CognitoHelper cognitoHelper) {
        if (client == null) {
            AWSConfiguration awsConfig = new AWSConfiguration(context);

            client = AWSAppSyncClient.builder()
                    .context(context)
                    .credentialsProvider(cognitoHelper.getCredentialsProvider())
                    .region(Regions.US_EAST_2)
                    .serverUrl("https://eprshjssvremzduaz3oc4nhkle.appsync-api.us-east-2.amazonaws.com/graphql")
                    .build();
        }
        return client;
    }
}
