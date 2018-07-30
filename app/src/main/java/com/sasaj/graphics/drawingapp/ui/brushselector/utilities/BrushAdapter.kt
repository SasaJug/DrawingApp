package com.sasaj.graphics.drawingapp.ui.brushselector.utilities

import android.graphics.Color
import com.sasaj.graphics.drawingapp.domain.Brush

/**
 * Created by sjugurdzija on 6/28/2016.
 */
object BrushAdapter {

    var brush: Brush? = null
        set(value) {
            field = value
            size = value!!.size
            blur = value.blur
            alpha = Color.alpha(value.color)
            Color.colorToHSV(value.color, hsv)
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
        set(value) {
            field = value
            hsv[0] = value
            brush?.color = Color.HSVToColor(hsv)
        }

    var saturation: Float = 0.0f
        set(value) {
            field = value
            hsv[1] = value
            brush?.color = Color.HSVToColor(hsv)
        }

    var brightness: Float = 0.0f
        set(value) {
            field = value
            hsv[2] = value
            brush?.color = Color.HSVToColor(hsv)
        }

    var hsv: FloatArray = FloatArray(3)

}
