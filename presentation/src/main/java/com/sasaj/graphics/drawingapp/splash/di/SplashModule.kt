package com.sasaj.graphics.drawingapp.splash.di

import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.CheckIfLoggedIn
import com.sasaj.graphics.drawingapp.common.ASyncTransformer
import com.sasaj.graphics.drawingapp.splash.SplashVMFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class SplashModule {

    @Provides
    fun provideCheckIfLoggedInUseCase(userRepository: UserRepository): CheckIfLoggedIn {
        return CheckIfLoggedIn(ASyncTransformer(), userRepository)
    }

    @Provides
    fun provideSplashVMFactory(checkIfLoggedIn: CheckIfLoggedIn): SplashVMFactory {
        return SplashVMFactory(checkIfLoggedIn)
    }
}