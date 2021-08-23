package com.sasaj.graphics.drawingapp.authentication.register.di

import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.SignUp
import com.sasaj.graphics.drawingapp.common.ASyncTransformer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RegisterModule {

    @Provides
    fun provideSignUpUseCase(userRepository: UserRepository): SignUp {
        return SignUp(ASyncTransformer(), userRepository)
    }

//    @Provides
//    fun provideRegisterVMFactory(signUpUseCase: SignUp): RegisterVMFactory {
//        return RegisterVMFactory(signUpUseCase)
//    }
}