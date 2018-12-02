package com.sasaj.domain.usecases

import com.sasaj.domain.BrushRepository
import com.sasaj.domain.common.Transformer
import com.sasaj.graphics.drawingapp.domain.Brush
import io.reactivex.Observable

class GetBrush(
        transformer: Transformer<Brush>,
        private val brushRepository: BrushRepository) : UseCase<Brush>(transformer) {

    fun getLastBrush(): Observable<Brush> {
        return observable()
    }

    override fun createObservable(data: Map<String, Any>?): Observable<Brush> {
            return brushRepository.getCurrentBrush()
    }
}