package com.sasaj.data.repositories


import android.util.Log
import com.sasaj.domain.DrawingRepository
import com.sasaj.domain.LocalFileManager
import com.sasaj.domain.entities.Drawing
import com.sasaj.domain.usecases.NetworkManager
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class DrawingRepositoryImpl(
    private val networkManager: NetworkManager,
    private val localDrawingRepository: LocalDrawingRepository,
    private val remoteDrawingRepository: RemoteDrawingRepository
) : DrawingRepository {

    override fun saveDrawing(localFileManager: LocalFileManager): Observable<Boolean> {
        return Observable.fromCallable {
            val drawing = localDrawingRepository.saveDrawing(localFileManager)
            if (networkManager.isConnected()) {
                remoteDrawingRepository.uploadDrawing(drawing)
            }
            true
        }
    }

    override fun getDrawings(): Observable<List<Drawing>> {
        return localDrawingRepository.getDrawings()
    }

    override fun getDrawingDetails(id: Long): Observable<Drawing> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun syncDrawings(): Observable<Boolean> {
        return Observable.fromCallable {

            var toUpload = hashSetOf<Drawing>()
            var toDownload = hashSetOf<Drawing>()

            val localDrawings = localDrawingRepository.getDrawingsFromDirectory()
            val remoteDrawings = remoteDrawingRepository.getListOfRemoteDrawings().blockingFirst()

            val localSet = localDrawings.toHashSet()
            val remoteSet = remoteDrawings.toHashSet()

            toDownload = HashSet<Drawing>(remoteSet)
            toDownload.removeAll(localSet)

            toUpload = HashSet<Drawing>(localSet)
            toUpload.removeAll(remoteSet)

            toDownload.forEach { drawing ->
                remoteDrawingRepository.downloadDrawing(drawing)
                    .observeOn(Schedulers.io())
                    .subscribe({ d: Drawing ->
                        localDrawingRepository.saveDrawingToDb(d)
                    },
                        { e ->
                            Log.e("DrawingRepo", e.localizedMessage)
                        }
                    ).dispose()
            }


            toUpload.forEach { drawing -> remoteDrawingRepository.uploadDrawing(drawing) }

            true
        }
    }

    companion object {
        private val TAG = DrawingRepositoryImpl::class.java.simpleName
    }
}
