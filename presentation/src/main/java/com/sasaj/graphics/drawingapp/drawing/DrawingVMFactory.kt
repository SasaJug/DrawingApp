package com.sasaj.graphics.drawingapp.drawing

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.sasaj.domain.usecases.GetBrush
import com.sasaj.domain.usecases.SaveBrush
import com.sasaj.domain.usecases.SaveDrawing

class DrawingVMFactory(private val saveDrawing: SaveDrawing,
                       private val getBrush: GetBrush,
                       private val saveBrush: SaveBrush) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DrawingViewModel(saveDrawing, getBrush, saveBrush) as T
    }
}