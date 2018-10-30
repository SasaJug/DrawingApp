package com.sasaj.graphics.drawingapp.repository

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.sasaj.graphics.drawingapp.R.id.save
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

class DrawingRepositoryImplementation(val db: AppDatabase, val transferUtility: TransferUtility) : DrawingRepository {

    companion object {
        val TAG = DrawingRepositoryImplementation::class.java.simpleName
    }

    override fun getDrawings(): Flowable<List<Drawing>> {
        return db.drawingDao().getAll()
    }

    override fun saveDrawing(bitmap: Bitmap?) {
        val path = saveFileLocally(bitmap)
        uploadWithTransferUtility(path)
    }

    private fun saveFileLocally(bitmap: Bitmap?): String? {
        val imageFile = getImageFile()
        val fos = FileOutputStream(imageFile)
        return try {
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
            val drawing = Drawing(imageFile.absolutePath, System.currentTimeMillis())
            db.drawingDao().insert(drawing)
            drawing.imagePath
        } catch (e: IOException) {
            Log.e(TAG, e.message)
            null
        } finally {
            fos.flush()
            fos.close()
        }
    }


    private fun uploadWithTransferUtility(path: String?) {

        val file = File(path)
        val uploadObserver = transferUtility.upload(path?.substring(path.lastIndexOf("/") + 1), file)

        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED)
                    Log.e(TAG, "onStateChanged: ")
                else if (state == TransferState.FAILED)
                    Log.e(TAG, "onStateChanged: upload failed")
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                Log.e(TAG, "onProgressChanged: $id")
            }

            override fun onError(id: Int, ex: Exception) {
                Log.e(TAG, "onError: ", ex)
            }
        })
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
