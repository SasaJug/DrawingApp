package com.sasaj.graphics.drawingapp.di

import com.sasaj.data.remote.AWSHelper
import com.sasaj.data.repositories.*
import com.sasaj.domain.DrawingRepository
import com.sasaj.domain.UserRepository
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class RepositoryModule {


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

}