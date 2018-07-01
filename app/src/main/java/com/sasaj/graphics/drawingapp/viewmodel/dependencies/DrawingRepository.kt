package com.sasaj.graphics.drawingapp.viewmodel.dependencies

import android.graphics.Bitmap

import com.sasaj.graphics.drawingapp.domain.Drawing

/**
 * Created by sjugurdzija on 4/21/2017.
 */

interface DrawingRepository {
    fun getDrawings(): List<Drawing>
    fun saveDrawing(bitmap: Bitmap?)
}
