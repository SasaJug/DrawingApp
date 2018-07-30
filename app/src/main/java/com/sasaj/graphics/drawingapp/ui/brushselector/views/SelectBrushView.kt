package com.sasaj.graphics.drawingapp.ui.brushselector.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.jakewharton.rxbinding2.widget.RxSeekBar
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.domain.Brush
import com.sasaj.graphics.drawingapp.ui.brushselector.utilities.BrushAdapter
import com.sasaj.graphics.drawingapp.ui.brushselector.utilities.BrushAdapter.brush
import com.sasaj.graphics.drawingapp.ui.brushselector.utilities.OnColorComponentSelectedListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.color_selector_view_layout.view.*
import kotlinx.android.synthetic.main.select_paint_view_layout.view.*

/**
 * Created by sjugurdzija on 6/25/2016.
 */
class SelectBrushView : LinearLayout {

    companion object {
        private val TAG = SelectBrushView::class.java.simpleName
    }

    private val brushAdapter: BrushAdapter = BrushAdapter

    var brushSelectorObservable: Subject<Brush> = PublishSubject.create<Brush>()


    private val selectionListener: OnColorComponentSelectedListener = object : OnColorComponentSelectedListener {

        override fun setSize(size: Int) {
            brushAdapter.size = size
            commonActions()
        }

        override fun setBlur(blur: Int) {
            brushAdapter.blur = blur.toFloat()
            commonActions()
        }

        override fun setAlpha(alpha: Int) {
            brushAdapter.alpha = alpha
            commonActions()
        }

        override fun setHue(hue: Int) {
            brushAdapter.hue = hue.toFloat()
            commonActions()
        }

        override fun setSaturation(saturation: Float) {
            brushAdapter.saturation = saturation
            commonActions()
        }

        override fun setBrightness(brightness: Float) {
            brushAdapter.brightness = brightness
            commonActions()
        }

        private fun commonActions() {
            brushAdapter.brush?.let {
                brushSample.setBrush(brushAdapter.brush)
                brushSelectorObservable.onNext(brushAdapter.brush!!)
            }

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
    }

    fun getSelectorObservable(): Observable<Brush> {
        return brushSelectorObservable
    }

    fun setCurrentBrush(brush: Brush) {
        brushAdapter.brush = brush
        brushSample.setBrush(brushAdapter.brush)
        setViews()
    }


    private fun setViews() {
        brushSizeSeekBar.progress = brushAdapter.size
        brushBlurSeekBar.progress = brushAdapter.blur.toInt()
        brushAlphaSeekBar.progress = brushAdapter.alpha
        brushColorSeekBar.progress = brushAdapter.hue.toInt()
        sbSelector.setColor(brushAdapter.hsv)
    }

}





















