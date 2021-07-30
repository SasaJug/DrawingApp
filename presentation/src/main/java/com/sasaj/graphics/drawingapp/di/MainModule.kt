package com.sasaj.graphics.drawingapp.di

import com.sasaj.domain.BrushRepository
import com.sasaj.domain.DrawingRepository
import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.*
import com.sasaj.graphics.drawingapp.common.ASyncTransformer
import com.sasaj.graphics.drawingapp.drawing.DrawingVMFactory
import com.sasaj.graphics.drawingapp.main.MainVMFactory
import com.sasaj.graphics.drawingapp.mappers.DrawingEntityToUIMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
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


    @Provides
    fun provideMainVMFactory(signOutUseCase: SignOut,
                             syncDrawingsUseCase: SyncDrawings,
                             getDrawingsUseCase : GetDrawings,
                             drawingEntityToUIMapper: DrawingEntityToUIMapper): MainVMFactory {
        return MainVMFactory(signOutUseCase, syncDrawingsUseCase, getDrawingsUseCase, drawingEntityToUIMapper)
    }


    @Provides
    fun provideDrawingVMFactory(saveDrawingUseCase: SaveDrawing,
                                getBrushUseCase: GetBrush,
                                saveBrushUseCase: SaveBrush): DrawingVMFactory {
        return DrawingVMFactory(saveDrawingUseCase, getBrushUseCase, saveBrushUseCase)
    }
}