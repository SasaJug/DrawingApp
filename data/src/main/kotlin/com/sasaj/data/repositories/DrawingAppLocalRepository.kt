package com.sasaj.data.repositories

import com.sasaj.data.database.AppDb
import com.sasaj.data.mappers.BrushDataMapper
import com.sasaj.data.mappers.BrushEntityMapper
import com.sasaj.domain.entities.Brush
import com.sasaj.domain.entities.Optional
import io.reactivex.Observable

class DrawingAppLocalRepository(private val entityToDataMapper: BrushEntityMapper,
                                private val dataToEntityMapper: BrushDataMapper,
                                private val db: AppDb) {

    fun getLastBrush(): Observable<Optional<Brush>> {
        return Observable.fromCallable {
            val brushDb = db.brushDao().getLastSaved()
            brushDb?.let {
                Optional.of(dataToEntityMapper.mapFrom(it))
            } ?: Optional.empty()
        }
    }

    fun saveBrush(brush: Brush): Observable<Boolean> {
        val brushDb = entityToDataMapper.mapFrom(brush)
        return Observable.fromCallable {
            val brushId = db.brushDao().insert(brushDb)
            return@fromCallable brushId > -1
        }
    }
}