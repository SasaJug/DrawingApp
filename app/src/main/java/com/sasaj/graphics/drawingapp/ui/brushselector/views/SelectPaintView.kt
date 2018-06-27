package com.sasaj.graphics.drawingapp.ui.brushselector.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar

import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.ui.brushselector.interfaces.PaintSelector
import com.sasaj.graphics.drawingapp.ui.brushselector.utilities.PaintWrapper
import com.sasaj.graphics.drawingapp.ui.brushselector.utilities.SimpleOnSeekBarChangeListener

/**
 * Created by sjugurdzija on 6/25/2016.
 */
class SelectPaintView : LinearLayout, PaintSelector {

    private lateinit var sbSelector: SaturationBrightnessSelector
    private lateinit var brushSample: BrushSample
    private lateinit var brushSizeSeekBar: SeekBar
    private lateinit var brushBlurSeekBar: SeekBar
    private lateinit var brushAlphaSeekBar: SeekBar
    private lateinit var brushColorSeekBar: SeekBar
    private lateinit var paintWrapper: PaintWrapper


    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        this.orientation = LinearLayout.VERTICAL
        val lif = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        lif.inflate(R.layout.select_paint_view_layout, this)

        paintWrapper = PaintWrapper

        brushSample = findViewById(R.id.brush_sample)
        sbSelector = findViewById(R.id.sb_selector)
        sbSelector.setColorPicker(this)

        brushSizeSeekBar = findViewById(R.id.brush_size_seekbar)
        brushBlurSeekBar = findViewById(R.id.brush_blur_seekbar)
        brushAlphaSeekBar = findViewById(R.id.brush_alpha_seekbar)
        brushColorSeekBar = findViewById(R.id.brush_color_seekbar)


        brushSizeSeekBar.setOnSeekBarChangeListener(object : SimpleOnSeekBarChangeListener() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                setProgressBar(SIZE, progress)
            }
        })

        brushBlurSeekBar.setOnSeekBarChangeListener(object : SimpleOnSeekBarChangeListener() {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                setProgressBar(BLUR, progress)
            }
        })

        brushAlphaSeekBar.setOnSeekBarChangeListener(object : SimpleOnSeekBarChangeListener() {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                setProgressBar(ALPHA, progress)
            }

        })

        brushColorSeekBar.setOnSeekBarChangeListener(object : SimpleOnSeekBarChangeListener() {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                setProgressBar(HUE, progress)
            }

        })

        setViews()
    }

    override fun getColor(): Int {
        return paintWrapper.getColor()
    }

    override fun setColor(color: Int) {
        paintWrapper.setColor(color)
        paintWrapper.setAlpha(brushAlphaSeekBar.progress)
        brushSample.setPaint(paintWrapper.paint)
    }

    private fun setViews() {
        brushSample.setPaint(paintWrapper.paint)
        brushSizeSeekBar.progress = paintWrapper.getSize()
        brushBlurSeekBar.progress = paintWrapper.getBlur().toInt()
        brushAlphaSeekBar.progress = paintWrapper.getAlpha()
        brushColorSeekBar.progress = paintWrapper.hsv!![0].toInt()
    }

    private fun setProgressBar(which: Int, value1: Int) {
        var value = value1

        when (which) {

            SIZE -> {
                paintWrapper.setSize(value)
                brushSample.setPaint(paintWrapper.paint)
            }

            BLUR -> {
                if (value == 0) {
                    value = 1
                }
                paintWrapper.setBlur(value.toFloat())
                brushSample.setPaint(paintWrapper.paint)
            }

            ALPHA -> {
                paintWrapper.setAlpha(value)
                brushSample.setPaint(paintWrapper.paint)
            }

            HUE -> sbSelector.setHue(value.toFloat())
        }
    }

    companion object {

        private val TAG = SelectPaintView::class.java.simpleName

        private val SIZE = 1
        private val BLUR = 2
        private val ALPHA = 3
        private val HUE = 4
    }
}





















