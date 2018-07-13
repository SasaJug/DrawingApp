package com.sasaj.graphics.drawingapp.ui.brushselector.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.jakewharton.rxbinding2.widget.RxSeekBar
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.domain.Brush
import com.sasaj.graphics.drawingapp.ui.brushselector.utilities.OnColorComponentSelectedListener
import com.sasaj.graphics.drawingapp.ui.brushselector.utilities.PaintWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.color_selector_view_layout.view.*
import kotlinx.android.synthetic.main.select_paint_view_layout.view.*

/**
 * Created by sjugurdzija on 6/25/2016.
 */
class SelectPaintView : LinearLayout {

    companion object {
        private val TAG = SelectPaintView::class.java.simpleName
    }

    private lateinit var brush: Brush
    private var paintWrapper: PaintWrapper? = null

    private val selectionListener: OnColorComponentSelectedListener = object : OnColorComponentSelectedListener {

        override fun setSize(size: Int) {
            paintWrapper?.size = size
            brushSample.invalidate()
        }

        override fun setBlur(blur: Int) {
            paintWrapper?.blur = blur.toFloat()
            brushSample.invalidate()
        }

        override fun setAlpha(alpha: Int) {
            paintWrapper?.alpha = alpha
            brushSample.invalidate()
        }

        override fun setHue(hue: Int) {
            paintWrapper?.hue = hue.toFloat()
            brushSample.invalidate()
        }

        override fun setSaturation(saturation: Float) {
            paintWrapper?.saturation = saturation
            brushSample.invalidate()
        }

        override fun setBrightness(brightness: Float) {
            paintWrapper?.brightness = brightness
            brushSample.invalidate()
        }
    }

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

        val sizeObservable = RxSeekBar.changes(brushSizeSeekBar)
        sizeObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { value -> selectionListener.setSize(value) }

        val blurObservable = RxSeekBar.changes(brushBlurSeekBar)
        blurObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { value -> selectionListener.setBlur(value) }

        val colorSelector: com.sasaj.graphics.drawingapp.ui.brushselector.views.ColorSelectorView = findViewById(R.id.colorSelector)
        colorSelector.setListener(selectionListener)

        setPaintValues()
        setViews()
    }

    private fun setPaintValues(){
        paintWrapper = PaintWrapper
        //Initial dummy values
        //TODO replace with values from saved brush
        paintWrapper!!.size  = 20
        paintWrapper!!.blur  = 5.0f
        paintWrapper!!.alpha = 250
        paintWrapper!!.hue = 200.0f
        paintWrapper!!.saturation= 0.5f
        paintWrapper!!.brightness= 0.5f
        brushSample.setPaint(paintWrapper?.paint)
    }

    private fun setViews() {
        brushSizeSeekBar.progress = paintWrapper!!.size
        brushBlurSeekBar.progress = paintWrapper!!.blur.toInt()
        brushAlphaSeekBar.progress = paintWrapper!!.alpha
        brushColorSeekBar.progress = paintWrapper!!.hue.toInt()
        sbSelector.setColor(paintWrapper!!.hsv)
    }

}





















