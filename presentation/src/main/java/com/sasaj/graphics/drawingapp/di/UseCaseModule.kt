package com.sasaj.graphics.drawingapp.di

import com.sasaj.domain.DrawingRepository
import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.*
import com.sasaj.graphics.drawingapp.common.ASyncTransformer
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class UseCaseModule {
    @Provides
    @Reusable
    fun providesGetBrushUseCase(brushRepository: com.sasaj.domain.BrushRepository): GetBrush {
        return GetBrush(ASyncTransformer(), brushRepository)
    }

    @Provides
    @Reusable
    fun providesSaveBrushUseCase(brushRepository: com.sasaj.domain.BrushRepository): SaveBrush {
        return SaveBrush(ASyncTransformer(), brushRepository)
    }


    @Provides
    @Reusable
    fun providesSaveDrawingUseCase(drawingRepository: DrawingRepository): SaveDrawing {
        return SaveDrawing(ASyncTransformer(), drawingRepository)
    }


    @Provides
    @Reusable
    fun providesGetDrawingsUseCase(drawingRepository: DrawingRepository): GetDrawings {
        return GetDrawings(ASyncTransformer(), drawingRepository)
    }


    @Provides
    @Reusable
    fun providesSyncDrawingsUseCase(drawingRepository: DrawingRepository): SyncDrawings {
        return SyncDrawings(ASyncTransformer(), drawingRepository)
    }

//    @Provides
//    @Reusable
//    fun providesCheckIfLoggedInUseCase(userRepository: UserRepository): CheckIfLoggedIn {
//        return CheckIfLoggedIn(ASyncTransformer(), userRepository)
//    }
//
//    @Provides
//    @Reusable
//    fun providesLoginUseCase(userRepository: UserRepository): LogIn {
//        return LogIn(ASyncTransformer(), userRepository)
//    }
//
//    @Provides
//    @Reusable
//    fun providesSignUpUseCase(userRepository: UserRepository): SignUp {
//        return SignUp(ASyncTransformer(), userRepository)
//    }

    @Provides
    @Reusable
    fun providesSignOutUseCase(userRepository: UserRepository): SignOut {
        return SignOut(ASyncTransformer(), userRepository)
    }
//
//    @Provides
//    @Reusable
//    fun providesVerifyUserUseCase(userRepository: UserRepository): VerifyUser {
//        return VerifyUser(ASyncTransformer(), userRepository)
//    }
//
//    @Provides
//    @Reusable
//    fun providesChangePassword(userRepository: UserRepository): ChangePassword {
//        return ChangePassword(ASyncTransformer(), userRepository)
//    }
//
//    @Provides
//    @Reusable
//    fun providesNewPassword(userRepository: UserRepository): NewPassword {
//        return NewPassword(ASyncTransformer(), userRepository)
//    }
}