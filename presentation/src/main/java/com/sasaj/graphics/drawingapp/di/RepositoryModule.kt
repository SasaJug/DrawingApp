package com.sasaj.graphics.drawingapp.di

import com.sasaj.data.remote.AWSHelper
import com.sasaj.data.repositories.*
import com.sasaj.domain.BrushRepository
import com.sasaj.domain.DrawingRepository
import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.NetworkManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun providesDrawingRepository(networkManager: NetworkManager,
                                  localDrawingRepository: LocalDrawingRepository,
                                  remoteDrawingRepository: RemoteDrawingRepository): DrawingRepository {
        return DrawingRepositoryImpl(networkManager, localDrawingRepository, remoteDrawingRepository)
    }

    @Provides
    @Singleton
    fun providesUserRepository(awsHelper: AWSHelper, localRepository: LocalRepository): UserRepository {
        return UserRepositoryImpl(awsHelper, localRepository)
    }


    @Provides
    @Singleton
    fun providesBrushRepository(localBrushRepository: LocalBrushRepository): BrushRepository {
        return BrushRepositoryImpl(localBrushRepository)
    }



}