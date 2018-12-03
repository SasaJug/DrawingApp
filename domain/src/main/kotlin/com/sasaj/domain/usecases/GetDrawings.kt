package com.sasaj.domain.usecases

import com.sasaj.domain.DrawingRepository
import com.sasaj.domain.common.Transformer
import com.sasaj.domain.entities.Drawing
import io.reactivex.Observable

class GetDrawings(
        transformer: Transformer<List<Drawing>>,
        private val drawingRepository: DrawingRepository) : UseCase<List<Drawing>>(transformer) {

    fun getDrawings(): Observable<List<Drawing>> {
        return observable()
    }

    override fun createObservable(data: Map<String, Any>?): Observable<List<Drawing>> {
            return drawingRepository.getDrawings()
    }
}