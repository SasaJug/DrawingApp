package com.sasaj.graphics.drawingapp.di

import android.content.Context
import com.sasaj.graphics.drawingapp.splash.SplashVMFactory
import com.sasaj.graphics.drawingapp.mappers.DrawingEntityToUIMapper
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class ApplicationModule(val context: Context) {

    @Provides
    @Reusable
    fun provideContext(): Context {
        return context
    }

    @Provides
    @Reusable
    fun providesDrawingEntityToUIMapper(): DrawingEntityToUIMapper{
        return DrawingEntityToUIMapper()
    }

    @Provides
    @Reusable
    fun providesSplashVMFactory(): SplashVMFactory {
        return SplashVMFactory()
    }
}