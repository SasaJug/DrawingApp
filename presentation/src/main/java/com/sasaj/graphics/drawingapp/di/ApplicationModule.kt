package com.sasaj.graphics.drawingapp.di

import android.content.Context
import android.net.ConnectivityManager
import com.sasaj.domain.usecases.NetworkManager
import com.sasaj.graphics.drawingapp.common.NetworkManagerImpl
import com.sasaj.graphics.drawingapp.mappers.DrawingEntityToUIMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

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
    fun provideDrawingEntityToUIMapper(): DrawingEntityToUIMapper {
        return DrawingEntityToUIMapper()
    }
}