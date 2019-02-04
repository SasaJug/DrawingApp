package com.sasaj.data.repositories

import com.sasaj.domain.BrushRepository
import com.sasaj.domain.entities.Brush
import com.sasaj.domain.entities.Optional
import io.reactivex.Observable

class BrushRepositoryImpl(private val localBrushRepository: LocalBrushRepository) : BrushRepository {

    override fun saveBrush(brush: Brush): Observable<Boolean> {
        return localBrushRepository.saveBrush(brush)
    }

    override fun getCurrentBrush(): Observable<Optional<Brush>> {
        return localBrushRepository.getLastBrush()
    }
}