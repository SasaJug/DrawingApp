package com.sasaj.graphics.drawingapp.common

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import com.sasaj.domain.LocalFileManager
import com.sasaj.domain.entities.Drawing
import com.sasaj.graphics.drawingapp.entities.DrawingUI
import com.sasaj.graphics.drawingapp.mappers.DrawingUIToEntityMapper
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class BitmapManager : LocalFileManager{


    var bitmap : Bitmap? = null

    override fun saveFileLocallyAndReturnEntity(directory : File): Drawing? {
        val timestamp = createTimestamp()
        val filename = "drawing_$timestamp.jpg"
        val imageFile = getImageFile(filename, directory)
        val imagePath = imageFile.absolutePath

        val fos = FileOutputStream(imagePath)
        return try {
            bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, fos)
            DrawingUIToEntityMapper().mapFrom(DrawingUI(-1, filename, imagePath, timestamp))
        } catch (e: IOException) {
            Log.e("BitmapManager", e.message)
            return null
        } finally {
            fos.flush()
            fos.close()
        }
    }


    private fun getImageFile(filename : String, directory : File): File {

        val file = File(directory, filename)
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        return file
    }

    private fun createTimestamp(): Long{
        return System.currentTimeMillis()
    }
}