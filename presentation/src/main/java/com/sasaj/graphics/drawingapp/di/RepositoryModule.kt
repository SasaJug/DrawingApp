package com.sasaj.graphics.drawingapp.di

import com.sasaj.data.aws.AWSHelper
import com.sasaj.graphics.drawingapp.common.AWSHelperImpl
import com.sasaj.data.repositories.*
import com.sasaj.domain.BrushRepository
import com.sasaj.domain.DrawingRepository
import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.NetworkManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun providesDrawingRepository(networkManager: NetworkManager,
                                  localDrawingRepository: LocalDrawingRepository,
                                  remoteDrawingRepository: RemoteDrawingRepository): DrawingRepository {
        return DrawingRepositoryImpl(networkManager, localDrawingRepository, remoteDrawingRepository)
    }

    @Provides
    fun providesUserRepository(awsHelper: AWSHelper, localRepository: LocalRepository): UserRepository {
        return UserRepositoryImpl(awsHelper, localRepository)
    }

    @Provides
    fun providesBrushRepository(localBrushRepository: LocalBrushRepository): BrushRepository {
        return BrushRepositoryImpl(localBrushRepository)
    }
}