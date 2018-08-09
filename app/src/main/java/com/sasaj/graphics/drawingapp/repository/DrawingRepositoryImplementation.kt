package com.sasaj.graphics.drawingapp.repository

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import com.sasaj.graphics.drawingapp.domain.Drawing
import com.sasaj.graphics.drawingapp.repository.database.AppDatabase
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.DrawingRepository
import io.reactivex.Flowable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by sjugurdzija on 4/21/2017.
 */

class DrawingRepositoryImplementation(val db: AppDatabase) : DrawingRepository {

    companion object {
        val TAG = DrawingRepositoryImplementation::class.java.simpleName
    }

    override fun getDrawings(): Flowable<List<Drawing>> {
        return db.drawingDao().getAll()
    }

    override fun saveDrawing(bitmap: Bitmap?) {
        try {
            val imageFile = getImageFile()
            val fos = FileOutputStream(imageFile)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            val drawing = Drawing(imageFile.absolutePath, System.currentTimeMillis())
            db.drawingDao().insert(drawing)
        } catch (e: IOException) {
            Log.e(TAG, e.message)
        }
    }

    private fun getImageFile(): File {
        val filename = createImageFileName()
        val ext = albumStorageDir()
        val extPath = ext.path
        val dir = File(extPath)

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

    private fun albumStorageDir(): File {
        val file = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "DrawingApp")
        if (!file.mkdirs()) {
        }
        return file
    }

    private fun createImageFileName(): String {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH).format(Date())
        return "drawing_$timeStamp.jpg"
    }
}
