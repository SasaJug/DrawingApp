package com.sasaj.graphics.drawingapp

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.plugins.RxJavaPlugins

/**
 * Created by sjugurdzija on 4/22/2017.
 */

@HiltAndroidApp
class DrawingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { e -> Log.e("App", e.message, e) }
    }
}
