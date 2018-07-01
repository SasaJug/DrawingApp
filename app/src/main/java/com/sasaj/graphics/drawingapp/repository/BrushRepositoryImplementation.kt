package com.sasaj.graphics.drawingapp.repository

import com.sasaj.graphics.drawingapp.domain.Brush
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.BrushRepository

class BrushRepositoryImplementation : BrushRepository{
    lateinit var brush: Brush

    override fun getCurrentBrush(): Brush {
        return brush
    }

    override fun saveCurrentBrush() {

    }
}