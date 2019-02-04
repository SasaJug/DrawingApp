package com.sasaj.graphics.drawingapp.di

import android.content.Context
import android.net.ConnectivityManager
import com.sasaj.data.mappers.BrushDataToEntityMapper
import com.sasaj.data.mappers.BrushEntityToDataMapper
import com.sasaj.data.mappers.DrawingDataToEntityMapper
import com.sasaj.data.mappers.DrawingEntityToDataMapper
import com.sasaj.data.remote.AWSHelper
import com.sasaj.domain.usecases.NetworkManager
import com.sasaj.graphics.drawingapp.common.NetworkManagerImpl
import com.sasaj.graphics.drawingapp.mappers.DrawingEntityToUIMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideConnectivityManager(): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideNetworkManager(connectivityManager: ConnectivityManager): NetworkManager {
        return NetworkManagerImpl(connectivityManager)
    }

    @Provides
    @Singleton
    fun providesAWSHelper(context: Context): AWSHelper {
        return AWSHelper(context)
    }

    @Provides
    @Singleton
    fun provideDrawingEntityToUIMapper(): DrawingEntityToUIMapper {
        return DrawingEntityToUIMapper()
    }

    @Provides
    @Singleton
    fun provideBrushEntityToDataMapper(): BrushEntityToDataMapper{
        return BrushEntityToDataMapper()
    }

    @Provides
    @Singleton
    fun provideBrushDataToEntityMapper(): BrushDataToEntityMapper{
        return BrushDataToEntityMapper()
    }

    @Provides
    @Singleton
    fun provideDrawingDataToEntityMapper(): DrawingDataToEntityMapper{
        return DrawingDataToEntityMapper()
    }

    @Provides
    @Singleton
    fun provideDrawingEntityToDataMapper(): DrawingEntityToDataMapper{
        return DrawingEntityToDataMapper()
    }
}