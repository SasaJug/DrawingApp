package com.sasaj.graphics.drawingapp.viewmodel

import android.graphics.Bitmap
import com.sasaj.domain.entities.Optional
import com.sasaj.domain.usecases.GetBrush
import com.sasaj.domain.usecases.SaveBrush
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.DrawingRepository
import io.reactivex.Observable
import javax.inject.Inject

class DrawingViewModel : BaseViewModel() {

    @Inject
    lateinit var drawingRepository: DrawingRepository

    @Inject
    lateinit var getBrush: GetBrush

    @Inject
    lateinit var saveBrush: SaveBrush

    fun saveDrawing(bitmap: Bitmap?) {
        // ToDo use RxKotlin
        val thread = Thread(Runnable { drawingRepository.saveDrawing(bitmap) })
        thread.start()
    }

    fun getLastBrush() : Observable<Optional<com.sasaj.domain.entities.Brush>> {
        return getBrush.getLastBrush()
    }

    fun saveBrush(brush: com.sasaj.domain.entities.Brush) : Observable<Boolean>  {
        return saveBrush.saveBrush(brush)
    }
}