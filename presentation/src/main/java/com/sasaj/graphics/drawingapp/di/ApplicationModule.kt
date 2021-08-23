package com.sasaj.graphics.drawingapp.di

import android.content.Context
import android.net.ConnectivityManager
import com.sasaj.data.aws.AWSHelper
import com.sasaj.data.mappers.BrushDataToEntityMapper
import com.sasaj.data.mappers.BrushEntityToDataMapper
import com.sasaj.data.mappers.DrawingDataToEntityMapper
import com.sasaj.data.mappers.DrawingEntityToDataMapper
import com.sasaj.graphics.drawingapp.common.AWSHelperImpl
import com.sasaj.domain.usecases.NetworkManager
import com.sasaj.graphics.drawingapp.common.NetworkManagerImpl
import com.sasaj.graphics.drawingapp.mappers.DrawingEntityToUIMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule{

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    fun provideNetworkManager(connectivityManager: ConnectivityManager): NetworkManager {
        return NetworkManagerImpl(connectivityManager)
    }

    @Provides
    fun providesAWSHelper(@ApplicationContext context: Context): AWSHelper {
        return AWSHelperImpl(context)
    }

    @Provides
    fun provideDrawingEntityToUIMapper(): DrawingEntityToUIMapper {
        return DrawingEntityToUIMapper()
    }

    @Provides
    fun provideBrushEntityToDataMapper(): BrushEntityToDataMapper{
        return BrushEntityToDataMapper()
    }

    @Provides
    fun provideBrushDataToEntityMapper(): BrushDataToEntityMapper{
        return BrushDataToEntityMapper()
    }

    @Provides
    fun provideDrawingDataToEntityMapper(): DrawingDataToEntityMapper{
        return DrawingDataToEntityMapper()
    }

    @Provides
    fun provideDrawingEntityToDataMapper(): DrawingEntityToDataMapper{
        return DrawingEntityToDataMapper()
    }
}