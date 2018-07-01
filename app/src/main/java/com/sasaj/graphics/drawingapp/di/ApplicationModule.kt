package com.sasaj.graphics.drawingapp.di

import com.sasaj.graphics.drawingapp.repository.BrushRepositoryImplementation
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.DrawingRepository
import com.sasaj.graphics.drawingapp.repository.DrawingRepositoryImplementation
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.BrushRepository
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
object ApplicationModule {

    @Provides
    @Reusable
    fun providesDrawingRepository(): DrawingRepository {
       return DrawingRepositoryImplementation()
    }

    @Provides
    @Reusable
    fun providesBrushRepository(): BrushRepository {
        return BrushRepositoryImplementation()
    }
}