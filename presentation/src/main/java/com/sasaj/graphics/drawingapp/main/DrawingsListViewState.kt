package com.sasaj.graphics.drawingapp.main

import com.sasaj.graphics.drawingapp.entities.DrawingUI

data class DrawingsListViewState(
        var listFetchStarted : Boolean  = false,
        var loading : Boolean = false,
        var isLoaded: Boolean = false,
        var list: List<DrawingUI>? = null
)