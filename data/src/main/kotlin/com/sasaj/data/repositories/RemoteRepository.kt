package com.sasaj.data.repositories

import CreateDrawingMutation
import android.os.Environment
import android.os.Looper
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
import com.sasaj.data.remote.CognitoHelper
import com.sasaj.domain.entities.Drawing
import io.reactivex.Single
import type.CreateDrawingInput
import ListDrawingsQuery
import type.TableDrawingFilterInput
import type.TableIDFilterInput
import java.io.File


class RemoteRepository(private val s3: AmazonS3Client,
                       private val transferUtility: TransferUtility,
                       private val appSyncClient: AWSAppSyncClient,
                       private val cognitoHelper: CognitoHelper) {

    companion object {
        const val TAG = "Remote Repository"
    }

    var drawing: Drawing? = null

    fun saveFileToDb(drawing: Drawing?) {
        this.drawing = drawing
        // Create the mutation request
        val createDrawingInput = CreateDrawingInput.builder()
                .id("empty")
                .title(drawing?.fileName)
                .description(drawing?.lastModified.toString())
                .userId(cognitoHelper!!.userPool.currentUser.userId)
                .fileName(drawing?.fileName)
                .build()

        val createDrawing = CreateDrawingMutation(createDrawingInput)

        // Enqueue the request (This will execute the request)

        appSyncClient.mutate(createDrawing).enqueue(addDrawingsCallback)
    }

    fun uploadWithTransferUtility(drawing: Drawing) {

        val uploadObserver = transferUtility.upload(drawing.fileName, File(drawing.imagePath))

        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
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


    private fun generatePresignedUrl(bucketName: String, objectKey: String?) {

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


    private fun getImageFile(filename: String): File {
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

    private fun createTimestamp(): Long {
        // Create an image timestamp
//        return SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH).format(Date())
        return System.currentTimeMillis()
    }


//    //get Part
//    override fun getDrawings(): Flowable<List<Drawing>> {
//        return db.drawingDao().getAll()
//    }


    // Sync part
    fun syncDrawings(localRepository: LocalRepository): Boolean {

        val filter = TableDrawingFilterInput.builder()
                .userId(TableIDFilterInput.builder().contains(cognitoHelper.userPool.currentUser.userId).build())
                .build()

        val listDrawings: ListDrawingsQuery = ListDrawingsQuery.builder()
                .filt(filter)
                .build()

        val listDrawingsCallback = object : GraphQLCall.Callback<ListDrawingsQuery.Data>() {

            override fun onResponse(response: Response<ListDrawingsQuery.Data>) {
                if (response.hasErrors()) {
                    Log.i(TAG, "onResponse: error")
                } else {
                    Log.i(TAG, "onResponse: success" + response.data())
                    val drawings: MutableList<ListDrawingsQuery.Item>? = response.data()?.listDrawings()?.items()
                    drawings?.forEach { item ->
                        if (!localRepository.getDrawingByFilename(item.fileName()!!).hasValue()) {
                            val file = getImageFile(item.fileName()!!)
                            val downloadObserver = transferUtility.download(item.fileName(), file)

                            downloadObserver.setTransferListener(object : TransferListener {
                                override fun onStateChanged(id: Int, state: TransferState) {
                                    if (state == TransferState.COMPLETED) {
                                        Log.i(TAG, "onStateChanged: Completed")
                                        val drawing = Drawing(-1,
                                                item.fileName()!!,
                                                file.absolutePath,
                                                (item.fileName()!!.substring(8, item.fileName()!!.indexOf('.'))).toLong())
                                        val thread = Thread(Runnable { localRepository.saveDrawing(drawing)})
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

        appSyncClient.query(listDrawings).enqueue(listDrawingsCallback)

        return true
    }



}