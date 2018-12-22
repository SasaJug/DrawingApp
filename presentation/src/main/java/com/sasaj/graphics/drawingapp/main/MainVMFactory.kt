package com.sasaj.graphics.drawingapp.main

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.sasaj.domain.usecases.GetDrawings
import com.sasaj.domain.usecases.SignOut
import com.sasaj.domain.usecases.SyncDrawings
import com.sasaj.graphics.drawingapp.mappers.DrawingEntityToUIMapper

class MainVMFactory(private val signOutUseCase: SignOut,
                    private val syncDrawings: SyncDrawings,
                    private val getDrawings: GetDrawings,
                    private val drawingEntityToUIMapper: DrawingEntityToUIMapper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(signOutUseCase, syncDrawings, getDrawings, drawingEntityToUIMapper) as T
    }
}