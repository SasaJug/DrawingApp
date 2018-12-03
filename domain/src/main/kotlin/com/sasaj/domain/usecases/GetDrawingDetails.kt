package com.sasaj.domain.usecases

import com.sasaj.domain.DrawingRepository
import com.sasaj.domain.common.Transformer
import com.sasaj.domain.entities.Drawing
import io.reactivex.Observable
import java.lang.IllegalArgumentException
import java.util.*

class GetDrawingDetails(
        transformer: Transformer<Drawing>,
        private val drawingRepository: DrawingRepository) : UseCase<Drawing>(transformer) {

    companion object {
        private const val PARAM_DRAWING_ID = "param:drawingId"
    }

    fun getDrawingDetails(id : Long): Observable<Drawing> {
        val data = HashMap<String, Long>()
        data[PARAM_DRAWING_ID] = id
        return observable(data)
    }

    override fun createObservable(data: Map<String, Any>?): Observable<Drawing> {
        val drawingId = data?.get(PARAM_DRAWING_ID)
        drawingId?.let {
            return drawingRepository.getDrawingDetails(it as Long)
        } ?: return Observable.error { IllegalArgumentException("DrawingId must be provided.") }
    }
}