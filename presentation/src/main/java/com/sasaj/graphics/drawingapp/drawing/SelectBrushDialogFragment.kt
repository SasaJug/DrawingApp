package com.sasaj.graphics.drawingapp.drawing

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.entities.BrushUI
import io.reactivex.Observable
import kotlinx.android.synthetic.main.select_brush_dialog_fragment_layout.*


/**
 * Created by sjugurdzija on 9/11/2016.
 */

class SelectBrushDialogFragment : DialogFragment() {

    private lateinit var brushSelectorObservable: Observable<BrushUI>
    lateinit var drawingNavigationViewModel: DrawingNavigationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            drawingNavigationViewModel = ViewModelProviders.of(it).get(DrawingNavigationViewModel::class.java)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.select_brush_dialog_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectBrushView.setCurrentBrush(requireArguments().getParcelable(BRUSH_PARCELABLE)!!)
        brushSelectorObservable = selectBrushView.getSelectorObservable()
        brushSelectorObservable
                .subscribe(
                        { br -> sendResult(br) },
                        { e -> Log.e(TAG, "Error creating brush.", e) },
                        { dismiss() }
                )
    }

    private fun sendResult(brush : BrushUI) {
        drawingNavigationViewModel.drawingNavigationLiveData.value = DrawingNavigationViewState(brush)
    }

    companion object {

        private val TAG = SelectBrushDialogFragment::class.java.simpleName
        const val BRUSH_PARCELABLE: String = "brush"

        fun newInstance(brush: BrushUI): SelectBrushDialogFragment {
            val fragment = SelectBrushDialogFragment()
            val bundle = Bundle()
            bundle.putParcelable(BRUSH_PARCELABLE, brush)
            fragment.arguments = bundle
            return fragment
        }
    }
}
