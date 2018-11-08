package com.sasaj.graphics.drawingapp.repository

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.apollographql.apollo.GraphQLCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.sasaj.graphics.drawingapp.aws.CognitoHelper
import com.sasaj.graphics.drawingapp.domain.Drawing
import com.sasaj.graphics.drawingapp.repository.database.AppDatabase
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.DrawingRepository
import io.reactivex.Flowable
import type.CreateDrawingInput
import CreateDrawingMutation
import ListDrawingsQuery
import type.TableDrawingFilterInput
import type.TableIDFilterInput
import type.TableStringFilterInput
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by sjugurdzija on 4/21/2017.
 */

class DrawingRepositoryImplementation(val db: AppDatabase,
                                      val transferUtility: TransferUtility,
                                      val appSyncClient : AWSAppSyncClient,
                                      val cognitoHelper: CognitoHelper) : DrawingRepository {

    companion object {
        val TAG = DrawingRepositoryImplementation::class.java.simpleName
    }

    override fun getDrawings(): Flowable<List<Drawing>> {

        val filter = TableDrawingFilterInput.builder().userId(TableIDFilterInput.builder().contains(cognitoHelper.userPool.currentUser.userId).build()).build()

        val listDrawings : ListDrawingsQuery = ListDrawingsQuery.builder()
                .filt(filter)
                .build()


        appSyncClient.query(listDrawings).enqueue(listDrawingsCallback)

        return db.drawingDao().getAll()
    }

    override fun saveDrawing(bitmap: Bitmap?) {
        val path = saveFileLocally(bitmap)
        uploadWithTransferUtility(path)
    }

    private fun saveFileLocally(bitmap: Bitmap?): String {
        val imageFile = getImageFile()
        val fos = FileOutputStream(imageFile)
        return try {
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
            val drawing = Drawing(imageFile.absolutePath, System.currentTimeMillis())
            db.drawingDao().insert(drawing)
            drawing.imagePath
        } catch (e: IOException) {
            Log.e(TAG, e.message)
            ""
        } finally {
            fos.flush()
            fos.close()
        }
    }

    private fun saveFileToDb(path: String){


        // Create the mutation request
        val createDrawingInput = CreateDrawingInput.builder()
                .id("empty")
                .title("first from app")
                .description("First img saved from app in table with userId")
                .userId(cognitoHelper.userPool.currentUser.userId)
                .fileName(path?.substring(path.lastIndexOf("/") + 1))
                .build()

        val createDrawing = CreateDrawingMutation(createDrawingInput)

        // Enqueue the request (This will execute the request)
        appSyncClient.mutate(createDrawing).enqueue(addDrawingsCallback)
    }


    private fun uploadWithTransferUtility(path: String) {

        val file = File(path)
        val uploadObserver = transferUtility.upload(path?.substring(path.lastIndexOf("/") + 1), file)

        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED){
                    Log.i(TAG, "onStateChanged: Completed")
                    saveFileToDb(path)
                } else if (state == TransferState.FAILED)
                    Log.i(TAG, "onStateChanged: upload failed")
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                Log.i(TAG, "onProgressChanged: $id")
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

    private val addDrawingsCallback = object : GraphQLCall.Callback<CreateDrawingMutation.Data>() {
        override fun onResponse(response: Response<CreateDrawingMutation.Data>) {
            if (response.hasErrors()) {
                Log.i(TAG, "onResponse: error")
            } else {
                Log.i(TAG, "onResponse: success")
            }
        }

        override fun onFailure(e: ApolloException) {
            Log.e(TAG, "Failed to make drawings api call", e)
            Log.e(TAG, e.message)
        }
    }


    private val listDrawingsCallback = object : GraphQLCall.Callback<ListDrawingsQuery.Data>() {
        override fun onResponse(response: Response<ListDrawingsQuery.Data>) {
            if (response.hasErrors()) {
                Log.i(TAG, "onResponse: error")
            } else {
                Log.i(TAG, "onResponse: success"+response.data().toString())
            }
        }

        override fun onFailure(e: ApolloException) {
            Log.e(TAG, "Failed to make drawings api call", e)
            Log.e(TAG, e.message)
        }
    }
}
