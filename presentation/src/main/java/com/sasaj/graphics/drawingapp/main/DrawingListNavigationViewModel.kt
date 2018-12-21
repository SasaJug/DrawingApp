package com.sasaj.graphics.drawingapp.main

import android.arch.lifecycle.MutableLiveData
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent

class DrawingListNavigationViewModel : BaseViewModel() {


    val drawingsListLiveData: MutableLiveData<DrawingsListNavigationViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    init {
        drawingsListLiveData.value = DrawingsListNavigationViewState()
    }


    companion object {
        private val TAG = DrawingListNavigationViewModel::class.java.simpleName
    }

}

