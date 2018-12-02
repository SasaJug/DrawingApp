package com.sasaj.domain

import com.sasaj.graphics.drawingapp.domain.Brush
import io.reactivex.Observable

interface BrushRepository {
    fun getCurrentBrush(): Observable<Brush>
    fun saveBrush(brush : Brush): Observable<Boolean>
}