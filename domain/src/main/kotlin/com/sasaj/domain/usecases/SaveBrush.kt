package com.sasaj.domain.usecases

import com.sasaj.domain.BrushRepository
import com.sasaj.domain.common.Transformer
import com.sasaj.domain.entities.Brush
import io.reactivex.Observable
import java.lang.IllegalArgumentException
import java.util.*

class SaveBrush(
        transformer: Transformer<Boolean>,
        private val brushRepository: BrushRepository) : UseCase<Boolean>(transformer) {

    companion object {
        private const val PARAM_BRUSH_ENTITY = "param:brushEntity"
    }

    fun saveBrush(brush : Brush): Observable<Boolean>{
        val data = HashMap<String, Brush>()
        data[PARAM_BRUSH_ENTITY] = brush
        return observable(data)
    }

    override fun createObservable(data: Map<String, Any>?): Observable<Boolean> {
        val brush = data?.get(PARAM_BRUSH_ENTITY)
        brush?.let {
            return brushRepository.saveBrush(it as Brush)
        } ?: return Observable.error { IllegalArgumentException("BrushEntity must be provided.") }
    }
}