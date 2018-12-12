package com.sasaj.graphics.drawingapp.ui.main

data class MainViewState(var state: Int = -1) {
    companion object {
        const val LOADING: Int = 0
        const val SIGNOUT_SUCCESSFUL: Int = 1
    }
}