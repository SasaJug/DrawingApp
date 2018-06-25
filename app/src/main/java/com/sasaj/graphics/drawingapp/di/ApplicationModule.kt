package com.sasaj.graphics.drawingapp.di

import com.sasaj.graphics.drawingapp.viewmodel.dependencies.DrawingsRepository
import com.sasaj.graphics.drawingapp.repository.DrawingsRepositoryImplementation
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
object ApplicationModule {

    @Provides
    @Reusable
    fun providesRepository(): DrawingsRepository {
       return DrawingsRepositoryImplementation()
    }
}