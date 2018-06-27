package com.sasaj.graphics.drawingapp.ui.brushselector.utilities

import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.Paint
import android.util.Log

/**
 * Created by sjugurdzija on 6/28/2016.
 */
object PaintWrapper {

    private var size: Int = 0
    private var blur: Float = 0.toFloat()
    private var alpha: Int = 0
    private var color: Int = 0
    var hsv: FloatArray? = null
        get() {
            Color.colorToHSV(color, field)
            Log.e("PaintWrapper", "getHsv() called with: " + field!![0] + field!![1] + field!![2])
            return field
        }

    var paint: Paint

    init {
        size = 10
        blur = 1f
        alpha = 255
        color = -0x1000000
        this.hsv = FloatArray(3)

        paint = Paint()
        paint.color = color
        paint.strokeWidth = size.toFloat()
        paint.alpha = alpha
        paint.maskFilter = BlurMaskFilter(blur, BlurMaskFilter.Blur.NORMAL)

        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
    }

    fun getSize(): Int {
        return size
    }

    fun setSize(size: Int) {
        this.size = size
        paint.strokeWidth = size.toFloat()
    }

    fun getBlur(): Float {
        return blur
    }

    fun setBlur(blur: Float) {
        this.blur = blur
        paint.maskFilter = BlurMaskFilter(blur, BlurMaskFilter.Blur.NORMAL)

    }

    fun getAlpha(): Int {
        return alpha
    }

    fun setAlpha(alpha: Int) {
        this.alpha = alpha
        paint.alpha = alpha
    }

    fun getColor(): Int {
        return color
    }

    fun setColor(color: Int) {
        this.color = color
        paint.color = color
    }
}
