package com.sasaj.graphics.drawingapp.main

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sasaj.domain.usecases.SignOut
import com.sasaj.domain.usecases.SyncDrawings
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import javax.inject.Inject

class MainViewModel : BaseViewModel() {

    @Inject
    lateinit var signOutUseCase: SignOut

    @Inject
    lateinit var syncDrawings: SyncDrawings

    val mainLiveData: MutableLiveData<MainViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    init {
        mainLiveData.value = MainViewState()
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

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }


}