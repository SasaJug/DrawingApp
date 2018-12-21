package com.sasaj.graphics.drawingapp.authentication.changepassword.di

import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.ChangePassword
import com.sasaj.domain.usecases.NewPassword
import com.sasaj.graphics.drawingapp.authentication.changepassword.ForgotPasswordVMFactory
import com.sasaj.graphics.drawingapp.common.ASyncTransformer
import dagger.Module
import dagger.Provides

@Module
class ForgotPasswordModule {

    @Provides
    @ForgotPasswordScope
    fun provideChangePassword(userRepository: UserRepository): ChangePassword {
        return ChangePassword(ASyncTransformer(), userRepository)
    }


    @Provides
    @ForgotPasswordScope
    fun provideNewPassword(userRepository: UserRepository): NewPassword {
        return NewPassword(ASyncTransformer(), userRepository)
    }

    @Provides
    @ForgotPasswordScope
    fun provideChangePasswordVMFactory(changePasswordUseCase: ChangePassword, newPasswordUseCase: NewPassword): ForgotPasswordVMFactory {
        return ForgotPasswordVMFactory(changePasswordUseCase, newPasswordUseCase)
    }


}