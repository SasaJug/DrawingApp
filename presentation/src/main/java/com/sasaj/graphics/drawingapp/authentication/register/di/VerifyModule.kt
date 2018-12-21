package com.sasaj.graphics.drawingapp.authentication.register.di

import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.VerifyUser
import com.sasaj.graphics.drawingapp.authentication.register.VerifyVMFactory
import com.sasaj.graphics.drawingapp.common.ASyncTransformer
import dagger.Module
import dagger.Provides

@Module
class VerifyModule {

    @Provides
    @VerifyScope
    fun provideVerifyUserUseCase(userRepository: UserRepository): VerifyUser {
        return VerifyUser(ASyncTransformer(), userRepository)
    }

    @Provides
    @VerifyScope
    fun provideVerifyVMFactory(verifyUserUseCase: VerifyUser): VerifyVMFactory {
        return VerifyVMFactory(verifyUserUseCase)
    }
}