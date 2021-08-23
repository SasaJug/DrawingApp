package com.sasaj.graphics.drawingapp.drawing

import androidx.lifecycle.MutableLiveData
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawingNavigationViewModel @Inject constructor(): BaseViewModel() {


    val drawingNavigationLiveData: MutableLiveData<DrawingNavigationViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    init {
        drawingNavigationLiveData.value = DrawingNavigationViewState()
    }

}