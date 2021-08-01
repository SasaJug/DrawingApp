package com.sasaj.graphics.drawingapp.drawing

import androidx.lifecycle.MutableLiveData
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent

class DrawingNavigationViewModel: BaseViewModel() {


    val drawingNavigationLiveData: MutableLiveData<DrawingNavigationViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    init {
        drawingNavigationLiveData.value = DrawingNavigationViewState()
    }

}