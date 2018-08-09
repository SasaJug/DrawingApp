package com.sasaj.graphics.drawingapp.viewmodel

import com.sasaj.graphics.drawingapp.domain.Drawing
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.DrawingRepository
import io.reactivex.Flowable
import javax.inject.Inject

class DrawingListViewModel : BaseViewModel() {


    @Inject
    lateinit var drawingRepository: DrawingRepository

    fun getDrawings(): Flowable<List<Drawing>> {
        return drawingRepository.getDrawings()
    }
}
