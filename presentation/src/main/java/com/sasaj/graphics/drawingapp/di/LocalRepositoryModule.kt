package com.sasaj.graphics.drawingapp.di

import android.arch.persistence.room.Room
import android.content.Context
import android.os.Environment
import com.sasaj.data.database.AppDb
import com.sasaj.data.mappers.BrushDataToEntityMapper
import com.sasaj.data.mappers.BrushEntityToDataMapper
import com.sasaj.data.mappers.DrawingDataToEntityMapper
import com.sasaj.data.mappers.DrawingEntityToDataMapper
import com.sasaj.data.remote.AWSHelper
import com.sasaj.data.repositories.LocalBrushRepository
import com.sasaj.data.repositories.LocalDrawingRepository
import com.sasaj.data.repositories.LocalRepository
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Singleton

@Module
class LocalRepositoryModule {

    @Provides
    @Singleton
    fun providesAppDatabase(context: Context): AppDb {
        return Room.databaseBuilder(context, AppDb::class.java, "DrawingAppDb").fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providesLocalRepository(db: AppDb): LocalRepository {
        return LocalRepository(BrushEntityToDataMapper(), BrushDataToEntityMapper(), DrawingDataToEntityMapper(), DrawingEntityToDataMapper(), db)
    }

    @Provides
    @Singleton
    fun providesLocalDrawingRepository(awsHelper : AWSHelper,
                                       drawingDataToEntityMapper: DrawingDataToEntityMapper,
                                       drawingEntityToDataMapper: DrawingEntityToDataMapper,
                                       db: AppDb): LocalDrawingRepository {
        return LocalDrawingRepository(awsHelper, drawingDataToEntityMapper, drawingEntityToDataMapper, db)
    }

    @Provides
    @Singleton
    fun providesLocalBrushRepository(brushEntityToDataMapper: BrushEntityToDataMapper,
                                     brushDataToEntityMapper: BrushDataToEntityMapper,
                                     db: AppDb): LocalBrushRepository {
        return LocalBrushRepository(brushEntityToDataMapper, brushDataToEntityMapper, db)
    }


}