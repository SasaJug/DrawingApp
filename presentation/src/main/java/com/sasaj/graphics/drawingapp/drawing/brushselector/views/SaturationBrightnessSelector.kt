package com.sasaj.graphics.drawingapp.drawing.brushselector.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.sasaj.graphics.drawingapp.drawing.brushselector.utilities.OnColorComponentSelectedListener

/**
 * Created by sjugurdzija on 7/17/2016.
 */
class SaturationBrightnessSelector : View {

    companion object {
        private val TAG = SaturationBrightnessSelector::class.java.simpleName
    }

    private var selectionListener: OnColorComponentSelectedListener? = null
    private var sourceWidth = 0
    private var sourceHeight = 0

    var hue: Float = 0.0f
        set(value) {
            field = value
            globalHsv[0] = value
            updateView()
        }
    var saturation: Float = 0.0f
    var brightness: Float = 0.0f

    private var cx: Float = 0.0f
    private var cy: Float = 0.0f
    private lateinit var backgroundPaint: Paint
    private lateinit var circlePaint: Paint
    private var factor: Float = 0.0f

    private val globalHsv = floatArrayOf(1f, 1f, 1f)
    private var brightnessGradient: Shader? = null
    private var saturationGradient: Shader? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {

        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        backgroundPaint = Paint()

        circlePaint = Paint()
        circlePaint.strokeWidth = 5f
        circlePaint.isAntiAlias = true
        circlePaint.style = Paint.Style.STROKE
        circlePaint.strokeJoin = Paint.Join.ROUND
        circlePaint.strokeCap = Paint.Cap.ROUND
    }

    fun setColor(hsv: FloatArray) {
        hue = hsv[0]
        saturation = hsv[1]
        brightness = hsv[2]
        updateView()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        sourceWidth = w
        sourceHeight = h
        factor = 1 / w.toFloat()
        cx = saturation / factor
        cy = (1 - brightness) / factor

        updateView()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRect(0f, 0f, this.measuredWidth.toFloat(), this.measuredHeight.toFloat(), backgroundPaint)
        canvas.drawCircle(cx, cy, 10f, circlePaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        cx = event.x
        cy = event.y
        saturation = cx * factor
        brightness = 1 - cy * factor
        selectionListener?.setSaturation(saturation)
        selectionListener?.setBrightness(brightness)

        updateView()

        return true
    }

    private fun updateView() {

        val rgb = Color.HSVToColor(globalHsv)

        //Update view's background parameters
        brightnessGradient = LinearGradient(0f, 0f, 0f, sourceHeight.toFloat(), -0x1, -0x1000000, Shader.TileMode.CLAMP)
        saturationGradient = LinearGradient(0f, 0f, sourceWidth.toFloat(), 0f, -0x1, rgb, Shader.TileMode.CLAMP)
        val shader = ComposeShader(brightnessGradient!!, saturationGradient!!, PorterDuff.Mode.MULTIPLY)
        backgroundPaint.shader = shader

        //Update indicator's parameters
        var circleHue = hue + 180
        if (circleHue > 360) circleHue -= 360
        val circleColor = Color.HSVToColor(floatArrayOf(circleHue, 1 - cx * factor, cy * factor))
        circlePaint.color = circleColor
        if (cx < 0) cx = 0f
        if (cx > sourceWidth) cx = sourceWidth.toFloat()
        if (cy < 0) cy = 0f
        if (cy > sourceHeight) cy = sourceHeight.toFloat()

        invalidate()
    }

    fun setListener(selectionListener: OnColorComponentSelectedListener?) {
        this.selectionListener = selectionListener
    }
}