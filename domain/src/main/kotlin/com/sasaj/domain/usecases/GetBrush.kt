package com.sasaj.domain.usecases

import com.sasaj.domain.BrushRepository
import com.sasaj.domain.common.Transformer
import com.sasaj.domain.entities.Brush
import com.sasaj.domain.entities.Optional
import io.reactivex.Observable

class GetBrush(
        transformer: Transformer<Optional<Brush>>,
        private val brushRepository: BrushRepository) : UseCase<Optional<Brush>>(transformer) {

    fun getLastBrush(): Observable<Optional<Brush>> {
        return observable()
    }

    override fun createObservable(data: Map<String, Any>?): Observable<Optional<Brush>> {
            return brushRepository.getCurrentBrush()
    }
}