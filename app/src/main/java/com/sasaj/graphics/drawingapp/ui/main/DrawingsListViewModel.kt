package com.sasaj.graphics.drawingapp.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sasaj.graphics.drawingapp.domain.Drawing
import com.sasaj.graphics.drawingapp.repository.DrawingsRepositoryImplementation

class DrawingsListViewModel : ViewModel(){
    val drawings = MutableLiveData<List<Drawing>>()

    private val repo: DrawingsRepositoryImplementation? = DrawingsRepositoryImplementation.getInstance()

    fun getDrawings(){
        drawings.postValue(repo?.drawings)
    }
}