package com.sasaj.graphics.drawingapp.di

import android.arch.persistence.room.Room
import android.content.Context
import com.sasaj.data.database.AppDb
import com.sasaj.data.mappers.BrushDataToEntityMapper
import com.sasaj.data.mappers.BrushEntityToDataMapper
import com.sasaj.data.mappers.DrawingDataToEntityMapper
import com.sasaj.data.mappers.DrawingEntityToDataMapper
import com.sasaj.data.repositories.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class LocalRepositoryModule {

    @Provides
    @Reusable
    fun providesAppDatabase(context: Context): AppDb {
        return Room.databaseBuilder(context, AppDb::class.java, "DrawingAppDb").fallbackToDestructiveMigration().build()
    }


    @Provides
    @Reusable
    fun providesLocalRepository(db: AppDb): LocalRepository {
        return LocalRepository(BrushEntityToDataMapper(), BrushDataToEntityMapper(), DrawingDataToEntityMapper(), DrawingEntityToDataMapper(), db)
    }


}