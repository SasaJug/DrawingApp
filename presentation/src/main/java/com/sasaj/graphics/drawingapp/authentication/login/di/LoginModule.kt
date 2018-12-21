package com.sasaj.graphics.drawingapp.authentication.login.di

import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.LogIn
import com.sasaj.domain.usecases.UseCase
import com.sasaj.graphics.drawingapp.authentication.login.LoginVMFactory
import com.sasaj.graphics.drawingapp.common.ASyncTransformer
import dagger.Module
import dagger.Provides

@Module
class LoginModule {

    @Provides
    @LoginScope
    fun provideLoginUseCase(userRepository: UserRepository): LogIn {
        return LogIn(ASyncTransformer(), userRepository)
    }

    @Provides
    @LoginScope
    fun provideLoginVMFactory(loginUseCase: LogIn): LoginVMFactory {
        return LoginVMFactory(loginUseCase)
    }
}