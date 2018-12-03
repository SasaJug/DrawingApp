package com.sasaj.domain

import com.sasaj.domain.entities.Brush
import com.sasaj.domain.entities.Optional
import io.reactivex.Observable

interface BrushRepository {
    fun getCurrentBrush(): Observable<Optional<Brush>>
    fun saveBrush(brush : Brush): Observable<Boolean>
}