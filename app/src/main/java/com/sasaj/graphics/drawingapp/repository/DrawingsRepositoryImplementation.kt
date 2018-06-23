package com.sasaj.graphics.drawingapp.repository

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log

import com.sasaj.graphics.drawingapp.domain.Drawing

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

/**
 * Created by sjugurdzija on 4/21/2017.
 */

object DrawingsRepositoryImplementation : DrawingsRepository {

    override val drawings: List<Drawing>
        get() {
            val dir = albumStorageDir()
            val drawings = ArrayList<Drawing>()
            val list = dir.listFiles()
            if (list != null && list.size > 0) {
                for (file in list) {
                    val drawing = Drawing(file.absolutePath, file.lastModified())
                    drawings.add(drawing)
                }

            }
            return drawings
        }

    fun getImageFile(): File {
        val filename = createImageFileName()
        val exst = albumStorageDir()
        val exstPath = exst.path
        val dir = File(exstPath)

        if (!dir.exists()) {
            try {
                dir.mkdir()
            } catch (se: SecurityException) {
            }

        }
        val file = File(dir, filename)
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        return file
    }



    fun albumStorageDir(): File{
        val file = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "DrawingApp")
        if (!file.mkdirs()) {
        }
        return file
    }




    override fun saveDrawing(bitmap: Bitmap?) {
        try {
            val imageFile = getImageFile()
            val fos = FileOutputStream(imageFile)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()


        } catch (e: IOException) {
            Log.e("error", e.message)
        }

    }


    private fun createImageFileName(): String {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH).format(Date())
        return "drawing_$timeStamp.jpg"
    }

}
