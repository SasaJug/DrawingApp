package com.sasaj.graphics.drawingapp.splash.di

import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.CheckIfLoggedIn
import com.sasaj.graphics.drawingapp.common.ASyncTransformer
import com.sasaj.graphics.drawingapp.splash.SplashVMFactory
import dagger.Module
import dagger.Provides

@Module
class SplashModule {

    @Provides
    @SplashScope
    fun provideCheckIfLoggedInUseCase(userRepository: UserRepository): CheckIfLoggedIn {
        return CheckIfLoggedIn(ASyncTransformer(), userRepository)
    }

    @Provides
    @SplashScope
    fun provideSplashVMFactory(checkIfLoggedIn: CheckIfLoggedIn): SplashVMFactory {
        return SplashVMFactory(checkIfLoggedIn)
    }
}