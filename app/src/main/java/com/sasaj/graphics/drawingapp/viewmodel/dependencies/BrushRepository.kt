package com.sasaj.graphics.drawingapp.viewmodel.dependencies

import com.sasaj.graphics.drawingapp.domain.Brush

interface BrushRepository {
    fun getCurrentBrush(): Brush
    fun saveCurrentBrush()
}