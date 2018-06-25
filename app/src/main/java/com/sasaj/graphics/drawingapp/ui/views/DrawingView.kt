package com.sasaj.graphics.drawingapp.ui.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by sjugurdzija on 6/25/2016
 */
class DrawingView : View {

    private var drawPath: Path? = null
    private var canvasPaint: Paint? = null
    private var drawCanvas: Canvas? = null
    var bitmapFromView: Bitmap? = null
        private set
    private var paint: Paint? = null
    private var xCoord: Float = 0.0f
    private var yCoord: Float = 0.0f

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
        drawPath = Path()
        canvasPaint = Paint(Paint.DITHER_FLAG)
    }

    fun setPaint(paint: Paint) {
        this.paint = paint
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmapFromView = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(bitmapFromView!!)

        val bgDrawable = background
        if (bgDrawable != null) {
            bgDrawable.draw(drawCanvas!!)
        } else {
            drawCanvas!!.drawColor(Color.WHITE)
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmapFromView!!, 0f, 0f, canvasPaint)
        canvas.drawPath(drawPath!!, paint!!)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart(x, y)
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }

    private fun touchStart(x: Float, y: Float) {
        drawPath!!.moveTo(x, y)
        this.xCoord = x
        this.yCoord = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - this.xCoord)
        val dy = Math.abs(y - this.yCoord)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            drawPath!!.quadTo(this.xCoord, this.yCoord, (x + this.xCoord) / 2, (y + this.yCoord) / 2)
            this.xCoord = x
            this.yCoord = y
        }
    }

    private fun touchUp() {
        drawCanvas!!.drawPath(drawPath!!, paint!!)
        drawPath!!.reset()
    }

    companion object {
        private val TAG = DrawingView::class.java.simpleName
        private val TOUCH_TOLERANCE = 4f
    }
}
