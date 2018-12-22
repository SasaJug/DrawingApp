package com.sasaj.graphics.drawingapp.di

import com.sasaj.graphics.drawingapp.drawing.DrawingActivity
import com.sasaj.graphics.drawingapp.main.MainActivity
import dagger.Subcomponent

@MainScope
@Subcomponent(modules = [MainModule::class])
interface MainSubcomponent {
    fun inject(mainActivity: MainActivity)
    fun inject(drawingActivity: DrawingActivity)
}