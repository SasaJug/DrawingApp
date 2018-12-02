package com.sasaj.domain.usecases

import com.sasaj.domain.DrawingRepository
import com.sasaj.domain.common.Transformer
import com.sasaj.graphics.drawingapp.domain.Drawing
import io.reactivex.Observable
import java.lang.IllegalArgumentException
import java.util.*

class SaveDrawing(
        transformer: Transformer<Boolean>,
        private val drawingRepository: DrawingRepository) : UseCase<Boolean>(transformer) {

    companion object {
        private const val PARAM_DRAWING_ENTITY = "param:drawingEntity"
    }

    fun saveBrush(drawing : Drawing): Observable<Boolean>{
        val data = HashMap<String, Drawing>()
        data[PARAM_DRAWING_ENTITY] = drawing
        return observable(data)
    }

    override fun createObservable(data: Map<String, Any>?): Observable<Boolean> {
        val drawing = data?.get(PARAM_DRAWING_ENTITY)
        drawing?.let {
            return drawingRepository.saveDrawing(it as Drawing)
        } ?: return Observable.error { IllegalArgumentException("DrawingEntity must be provided.") }
    }
}