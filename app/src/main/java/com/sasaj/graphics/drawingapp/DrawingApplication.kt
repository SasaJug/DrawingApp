package com.sasaj.graphics.drawingapp

import android.app.Application
import com.sasaj.graphics.drawingapp.di.ApplicationModule
import com.sasaj.graphics.drawingapp.di.DaggerViewModelInjector
import com.sasaj.graphics.drawingapp.di.ViewModelInjector

/**
 * Created by sjugurdzija on 4/22/2017.
 */

class DrawingApplication : Application(){
    companion object {
        lateinit var injector: ViewModelInjector
    }

    override fun onCreate() {
        super.onCreate()
        injector = DaggerViewModelInjector.builder()
                .applicationModule(ApplicationModule(applicationContext))
                .build()
    }
}
