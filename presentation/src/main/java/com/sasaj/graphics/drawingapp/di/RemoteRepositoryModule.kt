package com.sasaj.graphics.drawingapp.di

import android.content.Context
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import com.sasaj.data.remote.AWSHelper
import com.sasaj.data.remote.AppSyncClientFactory
import com.sasaj.data.repositories.RemoteRepository
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class RemoteRepositoryModule {

    @Provides
    @Reusable
    fun providesCognitoHelper(context: Context): AWSHelper {
        return AWSHelper(context)
    }

    @Provides
    @Reusable
    fun providesAppSyncClient(context: Context, awsHelper: AWSHelper): AWSAppSyncClient {
        return AppSyncClientFactory(context, awsHelper).client
    }

    @Provides
    @Reusable
    fun providesAmazonS3Client(AWSHelper: AWSHelper): AmazonS3Client {
        val s3 = AmazonS3Client(AWSHelper.credentialsProvider)
        s3.setRegion(Region.getRegion(AWSHelper.s3BucketRegion))
        return s3
    }


    @Provides
    @Reusable
    fun providesTransferUtility(context: Context, s3: AmazonS3Client, awsHelper: AWSHelper): TransferUtility {
        return TransferUtility.builder()
                .context(context)
                .s3Client(s3)
                .defaultBucket(awsHelper.s3BucketName)
                .build()
    }


    @Provides
    @Reusable
    fun providesRemoteRepository(s3: AmazonS3Client,
                                 transferUtility: TransferUtility,
                                 appSyncClient: AWSAppSyncClient,
                                 awsHelper: AWSHelper): RemoteRepository {
        return RemoteRepository(s3, transferUtility, appSyncClient, awsHelper)
    }

}