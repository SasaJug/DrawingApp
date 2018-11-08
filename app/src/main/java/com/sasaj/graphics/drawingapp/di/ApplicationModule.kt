package com.sasaj.graphics.drawingapp.di

import android.arch.persistence.room.Room
import android.content.Context
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.sasaj.graphics.drawingapp.aws.AppSyncClientFactory
import com.sasaj.graphics.drawingapp.aws.CognitoHelper
import com.sasaj.graphics.drawingapp.repository.AwsAuthRepositoryImplementation
import com.sasaj.graphics.drawingapp.repository.BrushRepositoryImplementation
import com.sasaj.graphics.drawingapp.repository.DrawingRepositoryImplementation
import com.sasaj.graphics.drawingapp.repository.database.APP_DATABASE_NAME
import com.sasaj.graphics.drawingapp.repository.database.AppDatabase
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.AuthRepository
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.BrushRepository
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.DrawingRepository
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class ApplicationModule(val context: Context) {

    @Provides
    @Reusable
    fun providesAppDatabase(): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, APP_DATABASE_NAME).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Reusable
    fun providesCognitoHelper(): CognitoHelper {
        return CognitoHelper(context)
    }

    @Provides
    @Reusable
    fun providesAppSyncClient(cognitoHelper: CognitoHelper): AWSAppSyncClient {
        return AppSyncClientFactory(context,cognitoHelper).client
    }

    @Provides
    @Reusable
    fun providesAmazonS3Client(cognitoHelper: CognitoHelper) : AmazonS3Client {
        val s3 = AmazonS3Client(cognitoHelper.credentialsProvider)
        s3.setRegion(Region.getRegion(Regions.US_EAST_2))
        return s3
    }


    @Provides
    @Reusable
    fun providesTransferUtility(s3: AmazonS3Client) :TransferUtility {
        return TransferUtility.builder()
                .context(context)
                .s3Client(s3)
                .defaultBucket("drawingappbucket")
                .build()
    }



    @Provides
    @Reusable
    fun providesDrawingRepository(db: AppDatabase, transferUtility: TransferUtility, appSyncClient: AWSAppSyncClient, cognitoHelper: CognitoHelper): DrawingRepository {
        return DrawingRepositoryImplementation(db, transferUtility, appSyncClient, cognitoHelper)
    }

    @Provides
    @Reusable
    fun providesBrushRepository(db: AppDatabase): BrushRepository {
        return BrushRepositoryImplementation(db)
    }


    @Provides
    @Reusable
    fun providesAuthRepository(): AuthRepository {
        return AwsAuthRepositoryImplementation(context)
    }
}