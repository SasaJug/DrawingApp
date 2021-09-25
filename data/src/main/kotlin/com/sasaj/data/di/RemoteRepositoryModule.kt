package com.sasaj.data.di

import android.content.Context
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import com.sasaj.data.aws.AWSHelper
import com.sasaj.data.repositories.RemoteDrawingRepository
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class RemoteRepositoryModule {


    @Provides
    fun providesAmazonS3Client(AWSHelper: AWSHelper): AmazonS3Client {
        val s3 = AmazonS3Client(AWSHelper.getCredentialsProvider())
        s3.setRegion(Region.getRegion(AWSHelper.getS3BucketRegion()))
        return s3
    }


    @Provides
    fun providesTransferUtility(@ApplicationContext context: Context, s3: AmazonS3Client, awsHelper: AWSHelper): TransferUtility {
        return TransferUtility.builder()
                .context(context)
                .s3Client(s3)
                .defaultBucket(awsHelper.getS3BucketName())
                .build()
    }


    @Provides
    fun providesRemoteDrawingRepository(s3: AmazonS3Client,
                                        transferUtility: TransferUtility,
                                        awsHelper: AWSHelper): RemoteDrawingRepository {
        return RemoteDrawingRepository(s3, transferUtility, awsHelper)
    }

}