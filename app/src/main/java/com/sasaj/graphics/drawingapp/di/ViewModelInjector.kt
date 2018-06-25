package com.sasaj.graphics.drawingapp.di

import com.sasaj.graphics.drawingapp.ui.drawing.DrawingViewModel
import com.sasaj.graphics.drawingapp.viewmodel.DrawingListViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(ApplicationModule::class)])
interface ViewModelInjector {
    /**
     * Injects required dependencies into the specified ViewModel.
     */
    fun inject(drawingListViewModel: DrawingListViewModel)
    fun inject(drawingViewModel: DrawingViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun applicationModule(applicationModule: ApplicationModule): Builder
    }
}