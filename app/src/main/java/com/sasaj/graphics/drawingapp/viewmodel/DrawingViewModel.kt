package com.sasaj.graphics.drawingapp.viewmodel

import android.graphics.Bitmap
import com.sasaj.graphics.drawingapp.viewmodel.BaseViewModel
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.DrawingsRepository
import javax.inject.Inject

class DrawingViewModel : BaseViewModel() {

    @Inject
    lateinit var drawingsRepository: DrawingsRepository

    fun saveDrawing(bitmap: Bitmap?) {
        val thread = Thread(Runnable { drawingsRepository.saveDrawing(bitmap) })
        thread.start()
    }
}