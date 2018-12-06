package com.sasaj.data.repositories


import com.sasaj.domain.DrawingRepository
import com.sasaj.domain.LocalFileManager
import com.sasaj.domain.entities.Drawing
import io.reactivex.Observable

class DrawingRepositoryImpl(private val localRepository : LocalRepository,
                            private val remoteRepository: RemoteRepository) : DrawingRepository{

    override fun saveDrawing(localFileManager: LocalFileManager): Observable<Boolean>{
        return Observable.fromCallable {
            val drawing = localFileManager.saveFileLocallyAndReturnEntity()
            localRepository.saveDrawing(drawing)
            remoteRepository.uploadWithTransferUtility(drawing!!)
            true
        }
    }

    override fun getDrawings(): Observable<List<Drawing>> {
        return localRepository.getDrawings()
    }

    override fun getDrawingDetails(id: Long): Observable<Drawing> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun syncDrawings(): Observable<Boolean> {
        return Observable.fromCallable {
            remoteRepository.syncDrawings(localRepository)
        }
    }
}