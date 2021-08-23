package com.sasaj.graphics.drawingapp.main

import androidx.lifecycle.MutableLiveData
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawingListNavigationViewModel @Inject constructor() : BaseViewModel() {


    val drawingsListLiveData: MutableLiveData<DrawingsListNavigationViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    init {
        drawingsListLiveData.value = DrawingsListNavigationViewState()
    }


    companion object {
        private val TAG = DrawingListNavigationViewModel::class.java.simpleName
    }

}

