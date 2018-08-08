package com.sasaj.graphics.drawingapp.ui.brushselector.utilities

import android.graphics.Color
import android.support.v4.graphics.ColorUtils
import android.util.Log
import com.sasaj.graphics.drawingapp.domain.Brush

/**
 * Created by sjugurdzija on 6/28/2016.
 */
object BrushAdapter {

    private val TAG: String = javaClass.simpleName

    var brush: Brush? = null
        set(value) {
            field = value
            size = value!!.size
            blur = value.blur
            Color.colorToHSV(value.color, hsv)
            alpha = Color.alpha(value.color)
        }

    /**
     * Brush diameter
     * range 0-100 Integer
     */
    var size: Int = 0
        set(value) {
            field = value
            brush?.size = value
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
            brush?.blur = field
        }

    var alpha: Int = 255
        set(value) {
            field = value
            brush?.color = Color.HSVToColor(value, hsv)
        }

    var hue: Float = 0.0f
        get() { return hsv[0]}
        set(value) {
            field = value
            hsv[0] = value
            brush?.color = Color.HSVToColor(alpha, hsv)
        }

    var saturation: Float = 0.0f
        set(value) {
            field = value
            hsv[1] = value
            brush?.color = Color.HSVToColor(alpha, hsv)
        }

    var brightness: Float = 0.0f
        set(value) {
            field = value
            hsv[2] = value
            brush?.color = Color.HSVToColor(alpha, hsv)
        }

    var hsv: FloatArray = FloatArray(3)

}
