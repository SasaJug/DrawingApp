package com.sasaj.graphics.drawingapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.sasaj.graphics.drawingapp.domain.Drawing
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.DrawingsRepository
import javax.inject.Inject

class DrawingListViewModel : BaseViewModel() {

    val drawings = MutableLiveData<List<Drawing>>()

    @Inject
    lateinit var drawingsRepository: DrawingsRepository


    fun getDrawings(){
        val thread = Thread(Runnable {  drawings.postValue(drawingsRepository.drawings) })
        thread.start()
    }


}