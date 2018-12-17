package com.sasaj.graphics.drawingapp.main

import com.sasaj.graphics.drawingapp.entities.DrawingUI

data class MainViewState(var state: Int = -1, var drawingsList: List<DrawingUI>? = null) {
    companion object {
        const val LOADING: Int = 0
        const val SIGNOUT_SUCCESSFUL: Int = 1
        const val SYNC_SUCCESSFUL: Int = 2
        const val DRAWINGS_LOADED: Int = 3
    }
}