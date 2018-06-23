package com.sasaj.graphics.drawingapp.ui.drawing

import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import com.sasaj.graphics.drawingapp.repository.DrawingsRepositoryImplementation

class DrawingViewModel : ViewModel() {
    private val repo: DrawingsRepositoryImplementation? = DrawingsRepositoryImplementation.getInstance()

    fun saveDrawing(bitmap: Bitmap) {
        repo?.saveDrawing(bitmap)
    }
}