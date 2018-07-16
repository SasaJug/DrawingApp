package com.sasaj.graphics.drawingapp.di

import com.sasaj.graphics.drawingapp.viewmodel.DrawingViewModel
import com.sasaj.graphics.drawingapp.viewmodel.DrawingListViewModel
import com.sasaj.graphics.drawingapp.viewmodel.SelectBrushViewModel
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
    fun inject(selectBrushViewModel: SelectBrushViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun applicationModule(applicationModule: ApplicationModule): Builder
    }
}