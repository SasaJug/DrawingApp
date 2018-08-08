package com.sasaj.graphics.drawingapp.ui.drawing

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.domain.Brush
import com.sasaj.graphics.drawingapp.ui.brushselector.views.SelectBrushView
import com.sasaj.graphics.drawingapp.viewmodel.SelectBrushViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.select_brush_dialog_fragment_layout.*

/**
 * Created by sjugurdzija on 9/11/2016.
 */

class SelectBrushDialogFragment : DialogFragment() {

    private lateinit var vm: SelectBrushViewModel
    private lateinit var brushSelectorObservable: Observable<Brush>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(this).get(SelectBrushViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.select_brush_dialog_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectBrushView.setCurrentBrush(vm.getCurrentBrush())
        brushSelectorObservable = selectBrushView.getSelectorObservable()
        brushSelectorObservable
                .observeOn(Schedulers.io())
                .subscribe (
                        {br -> vm.setBrush(br)},
                        {e -> Log.e(TAG, "Error creating brush.", e)},
                        {dismiss()}
                )
    }

    companion object {

        private val TAG = SelectBrushDialogFragment::class.java.simpleName

        fun newInstance(): SelectBrushDialogFragment {
            return SelectBrushDialogFragment()
        }
    }
}
