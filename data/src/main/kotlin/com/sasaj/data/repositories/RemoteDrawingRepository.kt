package com.sasaj.data.repositories

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.S3ObjectSummary
import com.sasaj.data.remote.AWSHelper
import com.sasaj.domain.entities.Drawing
import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import java.io.File


class RemoteDrawingRepository(private val s3: AmazonS3Client,
                              private val transferUtility: TransferUtility,
                              private val awsHelper: AWSHelper) {

    fun uploadDrawing(drawing: Drawing) {

        val objectMetadata = ObjectMetadata()
        objectMetadata.addUserMetadata("lastModified", drawing.lastModified.toString())
        val uploadObserver = transferUtility.upload(awsHelper.userPool.currentUser.userId + "/" + drawing.fileName, File(drawing.imagePath), objectMetadata)

        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                } else if (state == TransferState.FAILED) {
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
            }

            override fun onError(id: Int, ex: Exception) {
            }
        })
    }

    fun downloadDrawing(drawing: Drawing): Observable<Drawing> {

        val downloadSubject = ReplaySubject.create<Drawing>()
        val imageFile = getImageFile(drawing.fileName)
        val downloadObserver = transferUtility.download(awsHelper.userPool.currentUser.userId + "/" + drawing.fileName, imageFile)

        downloadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    downloadSubject.onNext(Drawing(fileName = drawing.fileName, imagePath = imageFile.absolutePath, lastModified = drawing.lastModified))
                } else if (state == TransferState.FAILED) {

                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
            }

            override fun onError(id: Int, ex: Exception) {
            }
        })

        return downloadSubject
    }

    fun getListOfRemoteDrawings(): Observable<List<Drawing>> {
        return io.reactivex.Observable.fromCallable {
            val list: MutableList<Drawing> = mutableListOf()
            val s3ObjList: List<S3ObjectSummary>? = s3.listObjects(awsHelper.s3BucketName, awsHelper.userPool.currentUser.userId).objectSummaries
            s3ObjList?.forEach { obj ->
                val fileName = obj.key.substringAfter('/')
                val lastModified = fileName.substring(8, fileName.indexOf('.')).toLong()
                list.add(Drawing(fileName = fileName, lastModified = lastModified))
            }
            list
        }
    }

    private fun getImageFile(filename: String): File {
        val dir = awsHelper.storageDirectory()

        val file = File(dir, filename)
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        return file
    }

    companion object {
        val TAG: String = RemoteDrawingRepository::class.java.simpleName
    }
}