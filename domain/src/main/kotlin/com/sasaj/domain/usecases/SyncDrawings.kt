package com.sasaj.domain.usecases

import com.sasaj.domain.DrawingRepository
import com.sasaj.domain.common.Transformer
import io.reactivex.Observable

class SyncDrawings(
        transformer: Transformer<Boolean>,
        private val drawingRepository: DrawingRepository) : UseCase<Boolean>(transformer) {


    fun syncDrawings(): Observable<Boolean>{
        return observable()
    }


    override fun createObservable(data: Map<String, Any>?): Observable<Boolean> {
        return drawingRepository.syncDrawings()
    }
}