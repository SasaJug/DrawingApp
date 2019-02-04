package com.sasaj.data.repositories

import android.util.Log
import com.sasaj.data.database.AppDb
import com.sasaj.data.mappers.DrawingDataToEntityMapper
import com.sasaj.data.mappers.DrawingEntityToDataMapper
import com.sasaj.data.remote.AWSHelper
import com.sasaj.domain.LocalFileManager
import com.sasaj.domain.entities.Drawing
import com.sasaj.domain.entities.Optional
import io.reactivex.Observable

class LocalDrawingRepository(private val awsHelper: AWSHelper,
                             private val drawingDataToEntityMapper: DrawingDataToEntityMapper,
                             private val drawingEntityToDataMapper: DrawingEntityToDataMapper,
                             private val db: AppDb) {

    fun getDrawings(): Observable<List<Drawing>> {
        return db.drawingDao().getAll().map { list -> drawingDataToEntityMapper.mapFromList(list) }.toObservable()
    }

    fun getDrawingByFilename(filename: String): Optional<Drawing> {
        val drawingDb = db.drawingDao().getByFilename(filename)
        drawingDb?.let {
            return Optional.of(drawingDataToEntityMapper.mapFrom(it))
        } ?: return Optional.empty()
    }

    fun getDrawingsFromDirectory(): List<Drawing> {
        deleteAll()
        val drawings = mutableListOf<Drawing>()
        val dir = awsHelper.storageDirectory()

        dir.list()?.forEach {
            val fileName = it.toString()
            val imagePath = "${dir.absolutePath}/$fileName"
            val lastModified = fileName.substring(8, fileName.indexOf('.')).toLong()
            val drawing = Drawing(fileName = fileName, imagePath = imagePath, lastModified = lastModified)
            saveDrawingToDb(drawing)
            drawings.add(drawing)
        }
        return drawings
    }

    fun saveDrawing(localFileManager: LocalFileManager): Drawing {
        val drawing = localFileManager.saveFileLocallyAndReturnEntity(awsHelper.storageDirectory())
        val drawingDb = drawingEntityToDataMapper.mapFrom(drawing!!)
        val drawingId = db.drawingDao().insert(drawingDb)
        return drawing
    }

    fun saveDrawingToDb(drawing: Drawing): Drawing {
        val drawingDb = drawingEntityToDataMapper.mapFrom(drawing!!)
        val drawingId = db.drawingDao().insert(drawingDb)
        return drawing
    }

    fun deleteAll() {
        db.drawingDao().deleteAll()
    }


    companion object {
        val TAG: String = LocalDrawingRepository::class.java.simpleName
    }
}