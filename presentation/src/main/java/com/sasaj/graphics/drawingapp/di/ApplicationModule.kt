package com.sasaj.graphics.drawingapp.di

import android.arch.persistence.room.Room
import android.content.Context
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.sasaj.data.database.APP_DB_NAME
import com.sasaj.data.database.AppDb
import com.sasaj.data.mappers.BrushDataMapper
import com.sasaj.data.mappers.BrushEntityMapper
import com.sasaj.data.repositories.BrushRepositoryImpl
import com.sasaj.data.repositories.DrawingAppLocalRepository
import com.sasaj.domain.entities.Brush
import com.sasaj.domain.entities.Optional
import com.sasaj.domain.usecases.GetBrush
import com.sasaj.domain.usecases.SaveBrush
import com.sasaj.graphics.drawingapp.aws.AppSyncClientFactory
import com.sasaj.graphics.drawingapp.aws.CognitoHelper
import com.sasaj.graphics.drawingapp.common.ASyncTransformer
import com.sasaj.graphics.drawingapp.repository.AwsAuthRepositoryImplementation
import com.sasaj.graphics.drawingapp.repository.DrawingRepositoryImplementation
import com.sasaj.graphics.drawingapp.repository.database.APP_DATABASE_NAME
import com.sasaj.graphics.drawingapp.repository.database.AppDatabase
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.AuthRepository
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
    fun providesDrawingRepository(db: AppDatabase, s3: AmazonS3Client, transferUtility: TransferUtility, appSyncClient: AWSAppSyncClient, cognitoHelper: CognitoHelper): DrawingRepository {
        return DrawingRepositoryImplementation(db, s3, transferUtility, appSyncClient, cognitoHelper)
    }


    @Provides
    @Reusable
    fun providesAuthRepository(): AuthRepository {
        return AwsAuthRepositoryImplementation(context)
    }


    @Provides
    @Reusable
    fun providesAppDb(): AppDb {
        return Room.databaseBuilder(context, AppDb::class.java, APP_DB_NAME).fallbackToDestructiveMigration().build()
    }


    @Provides
    @Reusable
    fun providesDrawingAppLocalRepository(appDb : AppDb): DrawingAppLocalRepository {
        return DrawingAppLocalRepository(BrushEntityMapper(), BrushDataMapper(), appDb)
    }

    @Provides
    @Reusable
    fun providesBrushRepository(drawingAppLocalRepository: DrawingAppLocalRepository): com.sasaj.domain.BrushRepository {
        return BrushRepositoryImpl(drawingAppLocalRepository)
    }

    @Provides
    @Reusable
    fun providesGetBrushUseCase(brushRepository : com.sasaj.domain.BrushRepository): GetBrush {
        return GetBrush(ASyncTransformer<Optional<Brush>>(), brushRepository)
    }

    @Provides
    @Reusable
    fun providesSaveBrushUseCase(brushRepository : com.sasaj.domain.BrushRepository): SaveBrush {
        return SaveBrush(ASyncTransformer<Boolean>(), brushRepository)
    }
}