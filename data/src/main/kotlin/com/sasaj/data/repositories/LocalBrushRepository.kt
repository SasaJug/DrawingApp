package com.sasaj.data.repositories

import com.sasaj.data.database.AppDb
import com.sasaj.data.mappers.BrushDataToEntityMapper
import com.sasaj.data.mappers.BrushEntityToDataMapper
import com.sasaj.domain.entities.Brush
import com.sasaj.domain.entities.Optional
import io.reactivex.Observable

class LocalBrushRepository(private val brushEntityToDataMapper: BrushEntityToDataMapper,
                           private val brushDataToEntityToMapper: BrushDataToEntityMapper,
                           private val db: AppDb) {


    fun getLastBrush(): Observable<Optional<Brush>> {
        return Observable.fromCallable {
            val brushDb = db.brushDao().getLastSaved()
            brushDb?.let {
                Optional.of(brushDataToEntityToMapper.mapFrom(it))
            } ?: Optional.empty()
        }
    }

    fun saveBrush(brush: Brush): Observable<Boolean> {
        val brushDb = brushEntityToDataMapper.mapFrom(brush)
        return Observable.fromCallable {
            val brushId = db.brushDao().insert(brushDb)
            return@fromCallable brushId > -1
        }
    }

    fun deleteAll(){
        db.brushDao().deleteAll()
    }
}