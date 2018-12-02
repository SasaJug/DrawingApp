package com.sasaj.graphics.drawingapp.viewmodel.dependencies

import com.sasaj.graphics.drawingapp.domain.Brush
import io.reactivex.Flowable

interface BrushRepository {
    fun getBrushFlowable(): Flowable<Brush>
    fun getCurrentBrush(): Brush
    fun setCurrentBrush(brush: Brush)
    fun saveCurrentBrush()
}