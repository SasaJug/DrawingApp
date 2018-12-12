package com.sasaj.graphics.drawingapp.drawing

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.entities.BrushUI
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.select_brush_dialog_fragment_layout.*


/**
 * Created by sjugurdzija on 9/11/2016.
 */

class SelectBrushDialogFragment : DialogFragment() {

    private lateinit var brushSelectorObservable: Observable<BrushUI>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.select_brush_dialog_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectBrushView.setCurrentBrush(arguments!!.getParcelable(BRUSH_PARCELABLE))
        brushSelectorObservable = selectBrushView.getSelectorObservable()
        brushSelectorObservable
                .observeOn(Schedulers.io())
                .subscribe(
                        { br -> sendResult(br) },
                        { e -> Log.e(TAG, "Error creating brush.", e) },
                        { dismiss() }
                )
    }

    private fun sendResult(brush : BrushUI) {
        val intent = Intent()
        intent.putExtra(BRUSH_PARCELABLE, brush)
        targetFragment!!.onActivityResult(
                targetRequestCode, RESULT_OK, intent)
    }

    companion object {

        private val TAG = SelectBrushDialogFragment::class.java.simpleName
        const val BRUSH_REQUEST_CODE: Int = 123
        const val BRUSH_PARCELABLE: String = "brush"

        fun newInstance(targetFragment: Fragment, brush: BrushUI): SelectBrushDialogFragment {
            val fragment = SelectBrushDialogFragment()
            fragment.setTargetFragment(targetFragment, BRUSH_REQUEST_CODE)
            val bundle = Bundle()
            bundle.putParcelable(BRUSH_PARCELABLE, brush)
            fragment.arguments = bundle
            return fragment
        }
    }
}
