package com.sasaj.graphics.drawingapp.di

import android.content.Context
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
    fun provideDrawingEntityToUIMapper(): DrawingEntityToUIMapper {
        return DrawingEntityToUIMapper()
    }

}