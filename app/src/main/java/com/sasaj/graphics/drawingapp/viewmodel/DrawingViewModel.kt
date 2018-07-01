package com.sasaj.graphics.drawingapp.viewmodel

import android.graphics.Bitmap
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.DrawingRepository
import javax.inject.Inject

class DrawingViewModel : BaseViewModel() {

    @Inject
    lateinit var drawingRepository: DrawingRepository

    fun saveDrawing(bitmap: Bitmap?) {
        val thread = Thread(Runnable { drawingRepository.saveDrawing(bitmap) })
        thread.start()
    }
}