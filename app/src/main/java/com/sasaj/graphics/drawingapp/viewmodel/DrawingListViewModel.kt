package com.sasaj.graphics.drawingapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.sasaj.graphics.drawingapp.domain.Drawing
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.DrawingRepository
import javax.inject.Inject

class DrawingListViewModel : BaseViewModel() {

    val drawings = MutableLiveData<List<Drawing>>()

    @Inject
    lateinit var drawingRepository: DrawingRepository


    fun getDrawings(){
        val thread = Thread(Runnable {  drawings.postValue(drawingRepository.getDrawings()) })
        thread.start()
    }
}