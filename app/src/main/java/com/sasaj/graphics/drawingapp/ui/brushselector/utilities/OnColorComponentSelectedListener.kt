package com.sasaj.graphics.drawingapp.ui.brushselector.utilities

interface OnColorComponentSelectedListener {

    fun setSize(size: Int)
    fun setBlur(blur: Int)
    fun setHue(hue: Int)
    fun setSaturation(saturation: Float)
    fun setBrightness(brightness: Float)
    fun setAlpha(alpha: Int)
}