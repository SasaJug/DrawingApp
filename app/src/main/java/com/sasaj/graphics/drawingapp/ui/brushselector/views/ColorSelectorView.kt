package com.sasaj.graphics.drawingapp.ui.brushselector.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.jakewharton.rxbinding2.widget.RxSeekBar
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.ui.brushselector.utilities.OnColorComponentSelectedListener
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.color_selector_view_layout.view.*


class ColorSelectorView : LinearLayout {

    private var selectionListener: OnColorComponentSelectedListener? = null

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
        this.orientation = LinearLayout.HORIZONTAL
        val lif = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        lif.inflate(R.layout.color_selector_view_layout, this)

        val colorObservable = RxSeekBar.changes(brushColorSeekBar)
        colorObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { value ->
                        sbSelector.hue = value.toFloat()
                        selectionListener?.setHue(value)
                    }

        val alphaObservable = RxSeekBar.changes(brushAlphaSeekBar)
        alphaObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { value -> selectionListener?.setAlpha(value) }

        setHueSeekBarBackground()
    }

    fun setListener(selectionListener: OnColorComponentSelectedListener) {
        this.selectionListener = selectionListener
        sbSelector.setListener(selectionListener)
    }

    private fun setHueSeekBarBackground() {
        val gd = GradientDrawable()

        // Set the color array to draw gradient
        gd.colors = intArrayOf(Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED)

        // Set the GradientDrawable gradient type linear gradient
        gd.gradientType = GradientDrawable.LINEAR_GRADIENT

        // Set GradientDrawable shape is a rectangle
        gd.shape = GradientDrawable.RECTANGLE

        // Set 3 pixels width solid blue color border
        gd.setStroke(3, Color.BLUE)

        gd.orientation = GradientDrawable.Orientation.LEFT_RIGHT

        // Set GradientDrawable as ImageView source image
        brushColorSeekBar.progressDrawable = gd
    }
}