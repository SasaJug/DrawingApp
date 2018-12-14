package com.sasaj.data.repositories

import com.sasaj.data.database.AppDb
import com.sasaj.data.entities.DrawingDb
import com.sasaj.data.mappers.BrushDataToEntityMapper
import com.sasaj.data.mappers.BrushEntityToDataMapper
import com.sasaj.data.mappers.DrawingDataToEntityMapper
import com.sasaj.data.mappers.DrawingEntityToDataMapper
import com.sasaj.domain.entities.Brush
import com.sasaj.domain.entities.Drawing
import com.sasaj.domain.entities.Optional
import io.reactivex.Observable

class LocalRepository(private val brushEntityToDataMapper: BrushEntityToDataMapper,
                      private val brushDataToEntityToMapper: BrushDataToEntityMapper,
                      private val drawingDataToEntityMapper: DrawingDataToEntityMapper,
                      private val drawingEntityToDataMapper: DrawingEntityToDataMapper,
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

    fun getDrawings(): Observable<List<Drawing>> {
           return  db.drawingDao().getAll().map { list -> toDrawings(list) }.toObservable()
    }

    fun getDrawingByFilename(filename : String) : Optional<Drawing> {
        val drawingDb = db.drawingDao().getByFilename(filename)
        drawingDb?.let {
           return Optional.of(drawingDataToEntityMapper.mapFrom(it))
        } ?: return Optional.empty()
    }

    fun saveDrawing(drawing: Drawing?): Boolean {
                val drawingDb = drawingEntityToDataMapper.mapFrom(drawing!!)
                val drawingId = db.drawingDao().insert(drawingDb)
                return drawingId > -1
    }

    private fun toDrawings(list : List<DrawingDb>) : List<Drawing>{
        val finalList = mutableListOf<Drawing>()
       list.forEach{drawingDb -> finalList.add(drawingDataToEntityMapper.mapFrom(drawingDb))}
        return finalList
    }

    fun deleteAll(){
        db.brushDao().deleteAll()
        db.drawingDao().deleteAll()
        db.clearAllTables()
    }
}