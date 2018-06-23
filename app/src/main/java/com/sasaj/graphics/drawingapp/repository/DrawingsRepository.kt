package com.sasaj.graphics.drawingapp.repository

import android.graphics.Bitmap

import com.sasaj.graphics.drawingapp.domain.Drawing

/**
 * Created by sjugurdzija on 4/21/2017.
 */

interface DrawingsRepository {

    val drawings: List<Drawing>
    fun saveDrawing(bitmap: Bitmap)
}
