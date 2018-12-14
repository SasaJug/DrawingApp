package com.sasaj.graphics.drawingapp.main

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sasaj.data.entities.DrawingDb
import com.sasaj.domain.entities.Drawing
import com.sasaj.domain.usecases.GetDrawings
import com.sasaj.domain.usecases.SyncDrawings
import com.sasaj.graphics.drawingapp.authentication.states.LoginViewState
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import com.sasaj.graphics.drawingapp.entities.DrawingUI
import com.sasaj.graphics.drawingapp.mappers.DrawingEntityToUIMapper
import io.reactivex.Observable
import javax.inject.Inject

class DrawingListViewModel : BaseViewModel() {


    @Inject
    lateinit var getDrawings: GetDrawings

    @Inject
    lateinit var drawingEntityToUIMapper: DrawingEntityToUIMapper

    val drawingsListLiveData: MutableLiveData<DrawingsListViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    init {
        drawingsListLiveData.value = DrawingsListViewState()
    }

    fun getDrawings() {
        val drawingsListViewState = drawingsListLiveData.value?.copy(listFetchStarted = true, loading = true, isLoaded = false, list = null)
        drawingsListLiveData.value = drawingsListViewState

        addDisposable(getDrawings.getDrawings()
                .map { list -> toDrawingsUI(list) }
                .subscribe(
                        { list: List<DrawingUI> ->
                            val newDrawingsListViewState = drawingsListLiveData.value?.copy(listFetchStarted = true, loading = false, isLoaded = true, list = list)
                            drawingsListLiveData.value = newDrawingsListViewState
                            errorState.value = null
                        },
                        { e ->
                            errorState.value = e
                        },
                        { Log.i(TAG, "Drawings list fetched") }
                )
        )
    }


    private fun toDrawingsUI(list : List<Drawing>) : List<DrawingUI>{
        val finalList = mutableListOf<DrawingUI>()
        list.forEach{drawing -> finalList.add(drawingEntityToUIMapper.mapFrom(drawing))}
        return finalList
    }

    companion object {
        private val TAG = DrawingListViewModel::class.java.simpleName
    }

}

