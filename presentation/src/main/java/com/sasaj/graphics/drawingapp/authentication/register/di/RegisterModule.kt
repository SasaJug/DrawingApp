package com.sasaj.graphics.drawingapp.authentication.register.di

import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.SignUp
import com.sasaj.graphics.drawingapp.authentication.register.RegisterVMFactory
import com.sasaj.graphics.drawingapp.common.ASyncTransformer
import dagger.Module
import dagger.Provides

@Module
class RegisterModule {

    @Provides
    @RegisterScope
    fun provideSignUpUseCase(userRepository: UserRepository): SignUp {
        return SignUp(ASyncTransformer(), userRepository)
    }

    @Provides
    @RegisterScope
    fun provideRegisterVMFactory(signUpUseCase: SignUp): RegisterVMFactory {
        return RegisterVMFactory(signUpUseCase)
    }
}