package com.sasaj.data.repositories


import com.sasaj.data.aws.AWSHelper
import com.sasaj.data.database.AppDb
import com.sasaj.data.mappers.DrawingDataToEntityMapper
import com.sasaj.data.mappers.DrawingEntityToDataMapper
import com.sasaj.domain.LocalFileManager
import com.sasaj.domain.entities.Drawing
import io.reactivex.Observable
import javax.inject.Inject

internal class LocalDrawingRepository @Inject constructor(
    private val awsHelper: AWSHelper,
    private val drawingDataToEntityMapper: DrawingDataToEntityMapper,
    private val drawingEntityToDataMapper: DrawingEntityToDataMapper,
    private val db: AppDb
) {

    fun getDrawings(): Observable<List<Drawing>> {
        return db.drawingDao().getAll().map { list -> drawingDataToEntityMapper.mapFromList(list) }
            .toObservable()
    }

    fun getDrawingsFromDirectory(): List<Drawing> {
        deleteAll()
        val drawings = mutableListOf<Drawing>()
        val dir = awsHelper.getStorageDirectory()

        dir.list().forEach {
            val fileName = it.toString()
            val imagePath = "${dir.absolutePath}/$fileName"
            val lastModified = fileName.substring(8, fileName.indexOf('.')).toLong()
            val drawing =
                Drawing(fileName = fileName, imagePath = imagePath, lastModified = lastModified)
            saveDrawingToDb(drawing)
            drawings.add(drawing)
        }
        return drawings
    }

    fun saveDrawing(localFileManager: LocalFileManager): Drawing {
        val drawing =
            localFileManager.saveFileLocallyAndReturnEntity(awsHelper.getStorageDirectory())
        val drawingDb = drawingEntityToDataMapper.mapFrom(drawing!!)
        db.drawingDao().insert(drawingDb)
        return drawing
    }

    fun saveDrawingToDb(drawing: Drawing): Drawing {
        val drawingDb = drawingEntityToDataMapper.mapFrom(drawing)
        db.drawingDao().insert(drawingDb)
        return drawing
    }

    private fun deleteAll() {
        db.drawingDao().deleteAll()
    }


    companion object {
        val TAG: String = LocalDrawingRepository::class.java.simpleName
    }
}