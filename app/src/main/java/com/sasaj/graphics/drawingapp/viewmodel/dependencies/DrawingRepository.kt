package com.sasaj.graphics.drawingapp.viewmodel.dependencies

import android.graphics.Bitmap

import com.sasaj.graphics.drawingapp.domain.Drawing
import io.reactivex.Flowable

/**
 * Created by sjugurdzija on 4/21/2017.
 */

interface DrawingRepository {
    fun getDrawings(): Flowable<List<Drawing>>
    fun saveDrawing(bitmap: Bitmap?)
}
