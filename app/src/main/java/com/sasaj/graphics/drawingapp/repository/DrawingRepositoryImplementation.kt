package com.sasaj.graphics.drawingapp.repository

import CreateDrawingMutation
import ListDrawingsQuery
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import com.amazonaws.AmazonServiceException
import com.amazonaws.HttpMethod
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.apollographql.apollo.GraphQLCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.sasaj.graphics.drawingapp.R.id.drawing
import com.sasaj.graphics.drawingapp.aws.CognitoHelper
import com.sasaj.graphics.drawingapp.domain.Drawing
import com.sasaj.graphics.drawingapp.repository.database.AppDatabase
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.DrawingRepository
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers.single
import type.CreateDrawingInput
import type.TableDrawingFilterInput
import type.TableIDFilterInput
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * Created by sjugurdzija on 4/21/2017.
 */

class DrawingRepositoryImplementation(private val db: AppDatabase,
                                      private val s3 : AmazonS3Client,
                                      private val transferUtility: TransferUtility,
                                      private val appSyncClient : AWSAppSyncClient,
                                      private val cognitoHelper: CognitoHelper) : DrawingRepository {

    companion object {
        val TAG = DrawingRepositoryImplementation::class.java.simpleName
    }

    override fun saveDrawing(bitmap: Bitmap?) {
        val timestamp = createTimestamp()
        val filename = "drawing_$timestamp.jpg"
        val imageFile = getImageFile(filename)

        val drawing = Drawing(filename, imageFile.absolutePath, timestamp)
        db.drawingDao().insert(drawing)

        val path = saveFileLocally(bitmap, drawing)

        uploadWithTransferUtility(drawing)
    }

    private fun saveFileLocally(bitmap: Bitmap?, drawing: Drawing): String {
        val fos = FileOutputStream(drawing.imagePath)
        return try {
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
            drawing.imagePath
        } catch (e: IOException) {
            Log.e(TAG, e.message)
            ""
        } finally {
            fos.flush()
            fos.close()
        }
    }

    private fun saveFileToDb(drawing: Drawing){

        // Create the mutation request
        val createDrawingInput = CreateDrawingInput.builder()
                .id("empty")
                .title(drawing.fileName)
                .description(drawing.lastModified.toString())
                .userId(cognitoHelper.userPool.currentUser.userId)
                .fileName(drawing.fileName)
                .build()

        val createDrawing = CreateDrawingMutation(createDrawingInput)

        // Enqueue the request (This will execute the request)
        appSyncClient.mutate(createDrawing).enqueue(addDrawingsCallback)
    }


    private fun uploadWithTransferUtility(drawing: Drawing) {

        val uploadObserver = transferUtility.upload(drawing.fileName, File(drawing.imagePath))

        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED){
                    Log.i(TAG, "onStateChanged: Completed")
                    saveFileToDb(drawing)
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


    private fun generatePresignedUrl(bucketName : String, objectKey: String?) {

        try {
            // Set the presigned URL to expire after one hour.
            val expiration = java.util.Date()
            var expTimeMillis = expiration.time
            expTimeMillis += (1000 * 60 * 60).toLong()
            expiration.time = expTimeMillis

            val generatePresignedUrlRequest = GeneratePresignedUrlRequest(bucketName, objectKey)
                    .withMethod(HttpMethod.GET)
                    .withExpiration(expiration)
            val url = s3.generatePresignedUrl(generatePresignedUrlRequest)

            Log.e(TAG, "Pre-Signed URL: " + url.toString())
        } catch (e: AmazonServiceException) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace()
        }
    }


    private fun getImageFile(filename : String): File {
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

    private fun createTimestamp(): Long{
        // Create an image timestamp
//        return SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH).format(Date())
        return System.currentTimeMillis()
    }


    //get Part
    override fun getDrawings(): Flowable<List<Drawing>> {
        return db.drawingDao().getAll()
    }


    // Sync part
    override fun syncDrawings(): Single<Boolean> {

        val filter = TableDrawingFilterInput.builder()
                .userId(TableIDFilterInput.builder().contains(cognitoHelper.userPool.currentUser.userId).build())
                .build()

        val listDrawings : ListDrawingsQuery = ListDrawingsQuery.builder()
                .filt(filter)
                .build()

        appSyncClient.query(listDrawings).enqueue(listDrawingsCallback)

        val single : Single<Boolean> = Single.create<Boolean> { emitter ->
            try {
                if (true) {
                    emitter.onSuccess(true)
                } else {
                    emitter.onError(RuntimeException())
                }
            } catch (e : Exception) {
                emitter.onError(e)
            }
        }

        return single
    }


    private val listDrawingsCallback = object : GraphQLCall.Callback<ListDrawingsQuery.Data>() {

        override fun onResponse(response: Response<ListDrawingsQuery.Data>) {
            if (response.hasErrors()) {
                Log.i(TAG, "onResponse: error")
            } else {
                Log.i(TAG, "onResponse: success"+response.data())
                val drawings: MutableList<ListDrawingsQuery.Item>? = response.data()?.listDrawings()?.items()
                drawings?.forEach { item ->
                    if(db.drawingDao().getByFilename(item.fileName())?.fileName == null){
                        val file = getImageFile(item.fileName()!!)
                        val downloadObserver = transferUtility.download(item.fileName(), file)

                        downloadObserver.setTransferListener(object : TransferListener {
                            override fun onStateChanged(id: Int, state: TransferState) {
                                if (state == TransferState.COMPLETED){
                                    Log.i(TAG, "onStateChanged: Completed")
                                    val drawing = Drawing(item.fileName()!!,
                                            file.absolutePath,
                                            (item.fileName()!!.substring(8, item.fileName()!!.indexOf('.'))).toLong())

                                    val thread = Thread(Runnable { db.drawingDao().insert(drawing)})
                                    thread.start()
                                } else if (state == TransferState.FAILED)
                                    Log.i(TAG, "onStateChanged: download failed")
                            }

                            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                                Log.i(TAG, "onProgressChanged: $id")
                            }

                            override fun onError(id: Int, ex: Exception) {
                                Log.e(TAG, "onError: ", ex)
                            }
                        })
                    }
                }
            }
        }

        override fun onFailure(e: ApolloException) {
            Log.e(TAG, "Failed to make drawings api call", e)
            Log.e(TAG, e.message)
        }
    }
}
