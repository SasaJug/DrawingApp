package com.sasaj.graphics.drawingapp.drawing

import android.graphics.Bitmap
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.common.setPaintParameters
import com.sasaj.graphics.drawingapp.entities.BrushUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_drawing.*

@AndroidEntryPoint
class DrawingFragment : Fragment() {

    private val drawingNavigationViewModel by activityViewModels<DrawingNavigationViewModel>()
    private val paint: Paint = Paint()
    var currentBrushUI: BrushUI = BrushUI()

    val bitmap: Bitmap?
        get() = drawing!!.bitmapFromView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        drawingNavigationViewModel.drawingNavigationLiveData.observe(viewLifecycleOwner, Observer{
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

    private fun handleViewState(drawingNavigationViewState: DrawingNavigationViewState?) {
        drawingNavigationViewState?.brushUI?.let {
            currentBrushUI = drawingNavigationViewState.brushUI!!
            paint.setPaintParameters(currentBrushUI)
            drawing?.setPaint(paint)
        }
    }

}
