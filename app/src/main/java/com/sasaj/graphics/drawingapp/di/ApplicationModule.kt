package com.sasaj.graphics.drawingapp.di

import android.arch.persistence.room.Room
import android.content.Context
import com.sasaj.graphics.drawingapp.repository.BrushRepositoryImplementation
import com.sasaj.graphics.drawingapp.repository.DrawingRepositoryImplementation
import com.sasaj.graphics.drawingapp.repository.database.APP_DATABASE_NAME
import com.sasaj.graphics.drawingapp.repository.database.AppDatabase
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.BrushRepository
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.DrawingRepository
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class ApplicationModule(val context: Context) {

    @Provides
    @Reusable
    fun providesAppDatabase(): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, APP_DATABASE_NAME).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Reusable
    fun providesDrawingRepository(db: AppDatabase): DrawingRepository {
       return DrawingRepositoryImplementation(db)
    }

    @Provides
    @Reusable
    fun providesBrushRepository(db: AppDatabase): BrushRepository {
        return BrushRepositoryImplementation(db)
    }
}