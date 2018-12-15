package com.sasaj.graphics.drawingapp.drawing

import com.sasaj.graphics.drawingapp.entities.BrushUI

data class DrawingViewState(
        val initialized: Boolean = false,
        val loading: Boolean = false,
        val bitmapSaved: Boolean = false,
        val brushSaved: Boolean = false,
        var brush: BrushUI? =  null
        )