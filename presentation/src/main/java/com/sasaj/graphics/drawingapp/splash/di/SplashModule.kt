package com.sasaj.graphics.drawingapp.splash.di

import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.CheckIfLoggedIn
import com.sasaj.graphics.drawingapp.common.ASyncTransformer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SplashModule {

    @Provides
    fun provideCheckIfLoggedInUseCase(userRepository: UserRepository): CheckIfLoggedIn {
        return CheckIfLoggedIn(ASyncTransformer(), userRepository)
    }

//    @Provides
////    @SplashScope
//    fun provideSplashVMFactory(checkIfLoggedIn: CheckIfLoggedIn): SplashVMFactory {
//        return SplashVMFactory(checkIfLoggedIn)
//    }
}