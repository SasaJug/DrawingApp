package com.sasaj.graphics.drawingapp.system

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import com.sasaj.graphics.drawingapp.domain.Brush


fun Paint.init(){
    this.isAntiAlias = true
    this.style = Paint.Style.STROKE
    this.strokeJoin = Paint.Join.ROUND
    this.strokeCap = Paint.Cap.ROUND
}

fun Paint.setBrush(brush: Brush?) {
    this.strokeWidth = brush!!.size.toFloat()
    this.maskFilter = BlurMaskFilter(brush.blur, BlurMaskFilter.Blur.NORMAL)
    this.color = brush.color
}