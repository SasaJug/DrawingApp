package com.sasaj.domain.usecases

import com.sasaj.domain.DrawingRepository
import com.sasaj.domain.LocalFileManager
import com.sasaj.domain.common.Transformer
import io.reactivex.Observable
import java.lang.IllegalArgumentException
import java.util.*

class SaveDrawing(
        transformer: Transformer<Boolean>,
        private val drawingRepository: DrawingRepository) : UseCase<Boolean>(transformer) {

    companion object {
        private const val PARAM_FILE_MANAGER_ENTITY = "param:fileManagerEntity"
    }

    fun saveDrawing(localFileManager: LocalFileManager): Observable<Boolean>{
        val data = HashMap<String, LocalFileManager>()
        data[PARAM_FILE_MANAGER_ENTITY] = localFileManager
        return observable(data)
    }

    override fun createObservable(data: Map<String, Any>?): Observable<Boolean> {
        val localFileManager= data?.get(PARAM_FILE_MANAGER_ENTITY)
        localFileManager?.let {
            return drawingRepository.saveDrawing(it as LocalFileManager)
        } ?: return Observable.error { IllegalArgumentException("Local file manager must be provided.") }
    }
}