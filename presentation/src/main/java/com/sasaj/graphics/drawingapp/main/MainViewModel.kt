package com.sasaj.graphics.drawingapp.main

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sasaj.domain.entities.Drawing
import com.sasaj.domain.usecases.GetDrawings
import com.sasaj.domain.usecases.SignOut
import com.sasaj.domain.usecases.SyncDrawings
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import com.sasaj.graphics.drawingapp.entities.DrawingUI
import com.sasaj.graphics.drawingapp.mappers.DrawingEntityToUIMapper
import javax.inject.Inject

class MainViewModel(private val signOutUseCase: SignOut,
                    private val syncDrawings: SyncDrawings,
                    private val getDrawings: GetDrawings,
                    private val drawingEntityToUIMapper: DrawingEntityToUIMapper) : BaseViewModel() {

    val mainLiveData: MutableLiveData<MainViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    init {
        mainLiveData.value = MainViewState()
    }


    fun getDrawings() {
        val mainViewState = mainLiveData.value?.copy(state = MainViewState.LOADING)
        mainLiveData.value = mainViewState

        addDisposable(getDrawings.getDrawings()
                .map { list -> toDrawingsUI(list) }
                .subscribe(
                        { list: List<DrawingUI> ->
                            val newDrawingsListViewState = mainLiveData.value?.copy(state = MainViewState.DRAWINGS_LOADED, drawingsList = list)
                            mainLiveData.value = newDrawingsListViewState
                            errorState.value = null
                        },
                        { e ->
                            errorState.value = e
                        },
                        { Log.i(TAG, "Drawings list fetched") }
                )
        )
    }

    fun signOut() {
        val mainViewState = mainLiveData.value?.copy(state = MainViewState.LOADING)
        mainLiveData.value = mainViewState

        addDisposable(signOutUseCase.signOut()
                .subscribe(
                        { b: Boolean ->
                            if(b){
                                val newMainViewState = mainLiveData.value?.copy(state = MainViewState.SIGNOUT_SUCCESSFUL)
                                mainLiveData.value = newMainViewState
                                errorState.value = null
                            }
                        },
                        { e ->
                            errorState.value = e
                        },
                        { Log.i(TAG, "Signout completed") }
                )
        )
    }

    fun syncData() {
        val mainViewState = mainLiveData.value?.copy(state = MainViewState.LOADING)
        mainLiveData.value = mainViewState

        addDisposable(syncDrawings.syncDrawings()
                .subscribe(
                        { b: Boolean ->
                            if(b){
                                val newMainViewState = mainLiveData.value?.copy(state = MainViewState.SYNC_SUCCESSFUL)
                                mainLiveData.value = newMainViewState
                                errorState.value = null
                            }
                        },
                        { e ->
                            errorState.value = e
                        },
                        { Log.i(TAG, "Sync completed") }
                )
        )
    }

    private fun toDrawingsUI(list : List<Drawing>) : List<DrawingUI>{
        val finalList = mutableListOf<DrawingUI>()
        list.forEach{drawing -> finalList.add(drawingEntityToUIMapper.mapFrom(drawing))}
        return finalList
    }

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }


}