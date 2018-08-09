package com.sasaj.graphics.drawingapp.viewmodel

import android.graphics.Bitmap
import com.sasaj.graphics.drawingapp.domain.Brush
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.BrushRepository
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.DrawingRepository
import io.reactivex.Flowable
import javax.inject.Inject

class DrawingViewModel : BaseViewModel() {

    @Inject
    lateinit var drawingRepository: DrawingRepository

    @Inject
    lateinit var brushRepository: BrushRepository

    fun saveDrawing(bitmap: Bitmap?) {
        // ToDo use RxKotlin
        val thread = Thread(Runnable { drawingRepository.saveDrawing(bitmap) })
        thread.start()
    }

    fun getBrush(): Flowable<Brush> {
        return brushRepository.getBrushFlowable()
    }
}