package com.sasaj.graphics.drawingapp.authentication.register.di

import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.VerifyUser
import com.sasaj.graphics.drawingapp.common.ASyncTransformer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class VerifyModule {

    @Provides
    fun provideVerifyUserUseCase(userRepository: UserRepository): VerifyUser {
        return VerifyUser(ASyncTransformer(), userRepository)
    }

//    @Provides
//    fun provideVerifyVMFactory(verifyUserUseCase: VerifyUser): VerifyVMFactory {
//        return VerifyVMFactory(verifyUserUseCase)
//    }
}