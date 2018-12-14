package com.sasaj.graphics.drawingapp.di

import android.arch.persistence.room.Room
import android.content.Context
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.sasaj.data.database.AppDb
import com.sasaj.data.mappers.BrushDataToEntityMapper
import com.sasaj.data.mappers.BrushEntityToDataMapper
import com.sasaj.data.mappers.DrawingDataToEntityMapper
import com.sasaj.data.mappers.DrawingEntityToDataMapper
import com.sasaj.data.remote.AppSyncClientFactory
import com.sasaj.data.remote.AWSHelper
import com.sasaj.data.repositories.*
import com.sasaj.domain.DrawingRepository
import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.*
import com.sasaj.graphics.drawingapp.common.ASyncTransformer
import com.sasaj.graphics.drawingapp.mappers.DrawingEntityToUIMapper
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class ApplicationModule(val context: Context) {


    @Provides
    @Reusable
    fun providesAppDatabase(): AppDb {
        return Room.databaseBuilder(context, AppDb::class.java, "DrawingAppDb").fallbackToDestructiveMigration().build()
    }


    @Provides
    @Reusable
    fun providesCognitoHelper(): AWSHelper {
        return AWSHelper(context)
    }

    @Provides
    @Reusable
    fun providesAppSyncClient(awsHelper: AWSHelper): AWSAppSyncClient {
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
    fun providesTransferUtility(s3: AmazonS3Client, awsHelper: AWSHelper): TransferUtility {
        return TransferUtility.builder()
                .context(context)
                .s3Client(s3)
                .defaultBucket(awsHelper.s3BucketName)
                .build()
    }


    @Provides
    @Reusable
    fun providesLocalRepository(db: AppDb): LocalRepository {
        return LocalRepository(BrushEntityToDataMapper(), BrushDataToEntityMapper(), DrawingDataToEntityMapper(), DrawingEntityToDataMapper(), db)
    }


    @Provides
    @Reusable
    fun providesRemoteRepository(s3: AmazonS3Client,
                                 transferUtility: TransferUtility,
                                 appSyncClient: AWSAppSyncClient,
                                 awsHelper: AWSHelper): RemoteRepository {
        return RemoteRepository(s3, transferUtility, appSyncClient, awsHelper)
    }


    @Provides
    @Reusable
    fun providesDrawingRepository(localRepository: LocalRepository, remoteRepository: RemoteRepository): DrawingRepository {
        return DrawingRepositoryImpl(localRepository, remoteRepository)
    }

    @Provides
    @Reusable
    fun providesUserRepository(awsHelper: AWSHelper, localRepository: LocalRepository): UserRepository {
        return UserRepositoryImpl(awsHelper, localRepository)
    }

    @Provides
    @Reusable
    fun providesBrushRepository(localRepository: LocalRepository): com.sasaj.domain.BrushRepository {
        return BrushRepositoryImpl(localRepository)
    }

    @Provides
    @Reusable
    fun providesGetBrushUseCase(brushRepository: com.sasaj.domain.BrushRepository): GetBrush {
        return GetBrush(ASyncTransformer(), brushRepository)
    }

    @Provides
    @Reusable
    fun providesSaveBrushUseCase(brushRepository: com.sasaj.domain.BrushRepository): SaveBrush {
        return SaveBrush(ASyncTransformer(), brushRepository)
    }


    @Provides
    @Reusable
    fun providesSaveDrawingUseCase(drawingRepository: DrawingRepository): SaveDrawing {
        return SaveDrawing(ASyncTransformer(), drawingRepository)
    }


    @Provides
    @Reusable
    fun providesGetDrawingsUseCase(drawingRepository: DrawingRepository): GetDrawings {
        return GetDrawings(ASyncTransformer(), drawingRepository)
    }


    @Provides
    @Reusable
    fun providesSyncDrawingsUseCase(drawingRepository: DrawingRepository): SyncDrawings {
        return SyncDrawings(ASyncTransformer(), drawingRepository)
    }

    @Provides
    @Reusable
    fun providesCheckIfLoggedInUseCase(userRepository: UserRepository): CheckIfLoggedIn {
        return CheckIfLoggedIn(ASyncTransformer(), userRepository)
    }

    @Provides
    @Reusable
    fun providesLoginUseCase(userRepository: UserRepository): LogIn {
        return LogIn(ASyncTransformer(), userRepository)
    }

    @Provides
    @Reusable
    fun providesSignUpUseCase(userRepository: UserRepository): SignUp {
        return SignUp(ASyncTransformer(), userRepository)
    }

    @Provides
    @Reusable
    fun providesSignOutUseCase(userRepository: UserRepository): SignOut {
        return SignOut(ASyncTransformer(), userRepository)
    }

    @Provides
    @Reusable
    fun providesVerifyUserUseCase(userRepository: UserRepository): VerifyUser {
        return VerifyUser(ASyncTransformer(), userRepository)
    }

    @Provides
    @Reusable
    fun providesChangePassword(userRepository: UserRepository): ChangePassword {
        return ChangePassword(ASyncTransformer(), userRepository)
    }

    @Provides
    @Reusable
    fun providesNewPassword(userRepository: UserRepository): NewPassword {
        return NewPassword(ASyncTransformer(), userRepository)
    }

    @Provides
    @Reusable
    fun providesDrawingEntityToUIMapper(): DrawingEntityToUIMapper{
        return DrawingEntityToUIMapper()
    }
}