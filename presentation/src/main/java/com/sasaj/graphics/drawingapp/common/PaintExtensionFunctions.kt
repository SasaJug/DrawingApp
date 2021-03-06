package com.sasaj.graphics.drawingapp.common

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import com.sasaj.graphics.drawingapp.entities.BrushUI

fun Paint.init() {
    this.isAntiAlias = true
    this.style = Paint.Style.STROKE
    this.strokeJoin = Paint.Join.ROUND
    this.strokeCap = Paint.Cap.ROUND
    this.strokeWidth = 5.toFloat()
    this.maskFilter = BlurMaskFilter(1.0f, BlurMaskFilter.Blur.NORMAL)
    this.color = 0xff000000.toInt()
}


fun Paint.setPaintParameters(brush: BrushUI) {
    this.init()
    brush?.let {
        this.strokeWidth = it.size.toFloat()
        this.maskFilter = BlurMaskFilter(it.blur, BlurMaskFilter.Blur.NORMAL)
        this.color = it.color
    }

}