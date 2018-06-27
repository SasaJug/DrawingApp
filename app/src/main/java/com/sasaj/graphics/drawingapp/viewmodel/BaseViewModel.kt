package com.sasaj.graphics.drawingapp.viewmodel

import android.arch.lifecycle.ViewModel
import com.sasaj.graphics.drawingapp.di.ApplicationModule
import com.sasaj.graphics.drawingapp.di.DaggerViewModelInjector
import com.sasaj.graphics.drawingapp.di.ViewModelInjector

abstract class BaseViewModel:ViewModel(){
    private val injector: ViewModelInjector = DaggerViewModelInjector.builder()
            .applicationModule(ApplicationModule)
            .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is DrawingListViewModel -> injector.inject(this)
            is DrawingViewModel -> injector.inject(this)
        }
    }
}