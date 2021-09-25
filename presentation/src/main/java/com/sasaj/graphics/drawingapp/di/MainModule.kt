package com.sasaj.graphics.drawingapp.di

import com.sasaj.domain.BrushRepository
import com.sasaj.domain.DrawingRepository
import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.*
import com.sasaj.graphics.drawingapp.common.ASyncTransformer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    fun provideGetBrushUseCase(brushRepository: BrushRepository): GetBrush {
        return GetBrush(ASyncTransformer(), brushRepository)
    }

    @Provides
    fun provideSaveBrushUseCase(brushRepository: BrushRepository): SaveBrush {
        return SaveBrush(ASyncTransformer(), brushRepository)
    }


    @Provides
    fun provideSaveDrawingUseCase(drawingRepository: DrawingRepository): SaveDrawing {
        return SaveDrawing(ASyncTransformer(), drawingRepository)
    }


    @Provides
    fun provideGetDrawingsUseCase(drawingRepository: DrawingRepository): GetDrawings {
        return GetDrawings(ASyncTransformer(), drawingRepository)
    }


    @Provides
    fun provideSyncDrawingsUseCase(drawingRepository: DrawingRepository): SyncDrawings {
        return SyncDrawings(ASyncTransformer(), drawingRepository)
    }

    @Provides
    fun provideSignOutUseCase(userRepository: UserRepository): SignOut {
        return SignOut(ASyncTransformer(), userRepository)
    }
}