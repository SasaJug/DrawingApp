package com.sasaj.domain

import com.sasaj.domain.entities.Drawing
import io.reactivex.Observable

interface DrawingRepository {
    fun getDrawings() : Observable<List<Drawing>>
    fun getDrawingDetails(id : Long) : Observable<Drawing>
    fun saveDrawing(localFileManager: LocalFileManager) : Observable<Boolean>
    fun syncDrawings() : Observable<Boolean>
}