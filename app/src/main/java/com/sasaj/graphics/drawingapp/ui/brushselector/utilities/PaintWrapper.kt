package com.sasaj.graphics.drawingapp.ui.brushselector.utilities

import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.Paint

/**
 * Created by sjugurdzija on 6/28/2016.
 */
object PaintWrapper {
    /**
     * Brush diameter
     * range 0-100 Integer
     */
    var size: Int = 0
        set(value) {
            field = value
            paint.strokeWidth = value.toFloat()
        }

    /**
     * Brush blur
     * range 0-50 Float
     */
    var blur: Float = 0f
        set(value) {
            field = if (value == 0f)
                1f
            else
                value
            paint.maskFilter = BlurMaskFilter(field, BlurMaskFilter.Blur.NORMAL)
        }

    var alpha: Int = 255
        set(value) {
            field = value
            paint.alpha = value
        }

    var hue: Float = 0.0f
        set(value) {
            field = value
            hsv[0] = value
            color = Color.HSVToColor(hsv)
            paint.color = color
        }

    var saturation: Float = 0.0f
        set(value) {
            field = value
            hsv[1] = value
            color = Color.HSVToColor(hsv)
            paint.color = color
        }

    var brightness: Float = 0.0f
        set(value) {
            field = value
            hsv[2] = value
            color = Color.HSVToColor(hsv)
            paint.color = color
        }

    var color: Int = 0
        get() {
            return Color.HSVToColor(hsv)
        }

    var hsv: FloatArray = FloatArray(3)
    var paint: Paint = Paint()

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
    }

}
