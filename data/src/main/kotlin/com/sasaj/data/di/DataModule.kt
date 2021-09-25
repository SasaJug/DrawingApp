package com.sasaj.data.di

import android.content.Context
import com.sasaj.data.aws.AWSHelper
import com.sasaj.data.aws.AWSHelperImpl
import com.sasaj.data.mappers.BrushDataToEntityMapper
import com.sasaj.data.mappers.BrushEntityToDataMapper
import com.sasaj.data.mappers.DrawingDataToEntityMapper
import com.sasaj.data.mappers.DrawingEntityToDataMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
internal class DataModule {

    @Provides
    fun providesAWSHelper(@ApplicationContext context: Context): AWSHelper {
        return AWSHelperImpl(context)
    }

    @Provides
    fun provideBrushEntityToDataMapper(): BrushEntityToDataMapper {
        return BrushEntityToDataMapper()
    }

    @Provides
    fun provideBrushDataToEntityMapper(): BrushDataToEntityMapper {
        return BrushDataToEntityMapper()
    }

    @Provides
    fun provideDrawingDataToEntityMapper(): DrawingDataToEntityMapper {
        return DrawingDataToEntityMapper()
    }

    @Provides
    fun provideDrawingEntityToDataMapper(): DrawingEntityToDataMapper {
        return DrawingEntityToDataMapper()
    }
}