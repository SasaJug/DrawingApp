package com.sasaj.graphics.drawingapp.authentication.login.di

import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.LogIn
import com.sasaj.domain.usecases.UseCase
import com.sasaj.graphics.drawingapp.authentication.login.LoginVMFactory
import com.sasaj.graphics.drawingapp.common.ASyncTransformer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class LoginModule {

    @Provides
    fun provideLoginUseCase(userRepository: UserRepository): LogIn {
        return LogIn(ASyncTransformer(), userRepository)
    }

    @Provides
    fun provideLoginVMFactory(loginUseCase: LogIn): LoginVMFactory {
        return LoginVMFactory(loginUseCase)
    }
}