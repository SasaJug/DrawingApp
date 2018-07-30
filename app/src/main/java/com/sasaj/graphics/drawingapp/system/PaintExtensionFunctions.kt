package com.sasaj.graphics.drawingapp.system

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import com.sasaj.graphics.drawingapp.domain.Brush


fun Paint.init(){
    this.isAntiAlias = true
    this.style = Paint.Style.STROKE
    this.strokeJoin = Paint.Join.ROUND
    this.strokeCap = Paint.Cap.ROUND
    this.strokeWidth = 5.toFloat()
    this.maskFilter = BlurMaskFilter(1.0f, BlurMaskFilter.Blur.NORMAL)
    this.color = 0xff000000.toInt()
}

fun Paint.setBrush(brush: Brush?) {
    brush?.let {
        this.strokeWidth = brush.size.toFloat()
        this.maskFilter = BlurMaskFilter(brush.blur, BlurMaskFilter.Blur.NORMAL)
        this.color = brush.color
    }

}