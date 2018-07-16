package com.sasaj.graphics.drawingapp.viewmodel.dependencies

import com.sasaj.graphics.drawingapp.domain.Brush
import io.reactivex.Observable

interface BrushRepository {
    fun getCurrentBrush(): Observable<Brush>
    fun setCurrentBrush(brush : Brush)
    fun saveCurrentBrush()
}