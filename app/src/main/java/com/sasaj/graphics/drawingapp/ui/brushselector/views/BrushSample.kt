package com.sasaj.graphics.drawingapp.ui.brushselector.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.sasaj.graphics.drawingapp.domain.Brush
import com.sasaj.graphics.drawingapp.system.init
import com.sasaj.graphics.drawingapp.system.setBrush

/**
 * Created by sjugurdzija on 6/25/2016.
 */
class BrushSample : View {
    private lateinit var path: Path
    private lateinit var paint: Paint
    private lateinit var bgPaint: Paint

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
        paint = Paint()
        paint.init()
    }

    fun setBrush(brush : Brush?) {
        paint.setBrush(brush)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = width / 4
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path = createPath(w, h)
        bgPaint = createCheckerBoard(h / 6)
    }

    private fun createPath(w: Int, h: Int): Path {
        val path = Path()
        val widthUnit = (w / 15).toFloat()
        val heightUnit = (h / 6).toFloat()

        path.moveTo(widthUnit * 2, heightUnit * 3)
        path.quadTo(widthUnit * 4, heightUnit * 2, widthUnit * 6, heightUnit * 3)
        path.quadTo(widthUnit * 8, heightUnit * 4, widthUnit * 10, heightUnit * 3)
        path.quadTo(widthUnit * 11.5f, heightUnit * 2.25f, widthUnit * 13, heightUnit * 2.75f)
        return path
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
        canvas.drawPath(path, paint)
    }

    private fun createCheckerBoard(pixelSize: Int): Paint {
        val bitmap = Bitmap.createBitmap(pixelSize * 2, pixelSize * 2, Bitmap.Config.ARGB_8888)

        val fill = Paint(Paint.ANTI_ALIAS_FLAG)
        fill.style = Paint.Style.FILL
        fill.color = 0x22000000

        val canvas = Canvas(bitmap)
        val rect = Rect(0, 0, pixelSize, pixelSize)
        canvas.drawRect(rect, fill)
        rect.offset(pixelSize, pixelSize)
        canvas.drawRect(rect, fill)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.shader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        return paint
    }
}
