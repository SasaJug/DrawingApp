package com.sasaj.graphics.drawingapp.di

import com.sasaj.domain.DrawingRepository
import com.sasaj.domain.UserRepository
import com.sasaj.domain.usecases.*
import com.sasaj.graphics.drawingapp.common.ASyncTransformer
import com.sasaj.graphics.drawingapp.drawing.DrawingVMFactory
import com.sasaj.graphics.drawingapp.main.MainVMFactory
import com.sasaj.graphics.drawingapp.mappers.DrawingEntityToUIMapper
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    @MainScope
    fun provideGetBrushUseCase(brushRepository: com.sasaj.domain.BrushRepository): GetBrush {
        return GetBrush(ASyncTransformer(), brushRepository)
    }

    @Provides
    @MainScope
    fun provideSaveBrushUseCase(brushRepository: com.sasaj.domain.BrushRepository): SaveBrush {
        return SaveBrush(ASyncTransformer(), brushRepository)
    }


    @Provides
    @MainScope
    fun provideSaveDrawingUseCase(drawingRepository: DrawingRepository): SaveDrawing {
        return SaveDrawing(ASyncTransformer(), drawingRepository)
    }


    @Provides
    @MainScope
    fun provideGetDrawingsUseCase(drawingRepository: DrawingRepository): GetDrawings {
        return GetDrawings(ASyncTransformer(), drawingRepository)
    }


    @Provides
    @MainScope
    fun provideSyncDrawingsUseCase(drawingRepository: DrawingRepository): SyncDrawings {
        return SyncDrawings(ASyncTransformer(), drawingRepository)
    }

    @Provides
    @MainScope
    fun provideSignOutUseCase(userRepository: UserRepository): SignOut {
        return SignOut(ASyncTransformer(), userRepository)
    }


    @Provides
    @MainScope
    fun provideMainVMFactory(signOutUseCase: SignOut,
                             syncDrawingsUseCase: SyncDrawings,
                             getDrawingsUseCase : GetDrawings,
                             drawingEntityToUIMapper: DrawingEntityToUIMapper): MainVMFactory {
        return MainVMFactory(signOutUseCase, syncDrawingsUseCase, getDrawingsUseCase, drawingEntityToUIMapper)
    }


    @Provides
    @MainScope
    fun provideDrawingVMFactory(saveDrawingUseCase: SaveDrawing,
                                getBrushUseCase: GetBrush,
                                saveBrushUseCase: SaveBrush): DrawingVMFactory {
        return DrawingVMFactory(saveDrawingUseCase, getBrushUseCase, saveBrushUseCase)
    }
}