package com.sasaj.data.repositories

import com.sasaj.domain.BrushRepository
import com.sasaj.domain.entities.Brush
import com.sasaj.domain.entities.Optional
import io.reactivex.Observable

class BrushRepositoryImpl(private val localRepository : LocalRepository) : BrushRepository {

    override fun saveBrush(brush: Brush): Observable<Boolean> {
        return localRepository.saveBrush(brush)
    }

    override fun getCurrentBrush(): Observable<Optional<Brush>> {
        return localRepository.getLastBrush()
    }
}