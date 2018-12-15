package com.sasaj.graphics.drawingapp.drawing


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.common.setPaintParameters
import com.sasaj.graphics.drawingapp.entities.BrushUI
import kotlinx.android.synthetic.main.fragment_drawing.*

class DrawingFragment : Fragment() {

    lateinit var drawingNavigationViewModel: DrawingNavigationViewModel
    private val paint: Paint = Paint()
    var currentBrushUI: BrushUI = BrushUI()

    val bitmap: Bitmap?
        get() = drawing!!.bitmapFromView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            drawingNavigationViewModel = ViewModelProviders.of(it).get(DrawingNavigationViewModel::class.java)
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        drawingNavigationViewModel.drawingNavigationLiveData.observe(this, Observer {
            if (it != null) handleViewState(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_drawing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paint.setPaintParameters(currentBrushUI)
        drawing?.setPaint(paint)
    }


    private fun handleViewState(drawingNavigationViewState: DrawingNavigationViewState) {
        drawingNavigationViewState.brushUI?.let {
            currentBrushUI = drawingNavigationViewState.brushUI!!
            paint.setPaintParameters(currentBrushUI)
            drawing?.setPaint(paint)
        }
    }

}
