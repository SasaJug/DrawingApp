package com.sasaj.graphics.drawingapp.drawing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sasaj.domain.usecases.GetBrush
import com.sasaj.domain.usecases.SaveBrush
import com.sasaj.domain.usecases.SaveDrawing

//class DrawingVMFactory(private val saveDrawing: SaveDrawing,
//                       private val getBrush: GetBrush,
//                       private val saveBrush: SaveBrush) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return DrawingViewModel(saveDrawing, getBrush, saveBrush) as T
//    }
//}