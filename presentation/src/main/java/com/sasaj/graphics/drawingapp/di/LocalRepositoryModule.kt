package com.sasaj.graphics.drawingapp.di

import androidx.room.Room
import android.content.Context
import com.sasaj.data.aws.AWSHelper
import com.sasaj.data.database.AppDb
import com.sasaj.data.mappers.BrushDataToEntityMapper
import com.sasaj.data.mappers.BrushEntityToDataMapper
import com.sasaj.data.mappers.DrawingDataToEntityMapper
import com.sasaj.data.mappers.DrawingEntityToDataMapper
import com.sasaj.data.repositories.LocalBrushRepository
import com.sasaj.data.repositories.LocalDrawingRepository
import com.sasaj.data.repositories.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalRepositoryModule {

    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context): AppDb {
        return Room.databaseBuilder(context, AppDb::class.java, "DrawingAppDb").fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providesLocalRepository(db: AppDb): LocalRepository {
        return LocalRepository(db)
    }

    @Provides
    fun providesLocalDrawingRepository(awsHelper: AWSHelper,
                                       drawingDataToEntityMapper: DrawingDataToEntityMapper,
                                       drawingEntityToDataMapper: DrawingEntityToDataMapper,
                                       db: AppDb): LocalDrawingRepository {
        return LocalDrawingRepository(awsHelper, drawingDataToEntityMapper, drawingEntityToDataMapper, db)
    }

    @Provides
    fun providesLocalBrushRepository(brushEntityToDataMapper: BrushEntityToDataMapper,
                                     brushDataToEntityMapper: BrushDataToEntityMapper,
                                     db: AppDb): LocalBrushRepository {
        return LocalBrushRepository(brushEntityToDataMapper, brushDataToEntityMapper, db)
    }


}