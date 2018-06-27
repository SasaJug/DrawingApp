package com.sasaj.graphics.drawingapp.ui.drawing

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window

import com.sasaj.graphics.drawingapp.R

/**
 * Created by sjugurdzija on 9/11/2016.
 */

class SelectPaintDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        return inflater.inflate(R.layout.select_paint_dialog_fragment_layout, container, false)
    }

    companion object {

        private val TAG = SelectPaintDialogFragment::class.java.simpleName


        private fun newInstance(index: Int): SelectPaintDialogFragment {

            val df = SelectPaintDialogFragment()
            val args = Bundle()
            args.putInt("index", index)
            df.arguments = args
            return df
        }

        fun newInstance(bundle: Bundle): SelectPaintDialogFragment {
            val index = bundle.getInt("index", 0)
            return newInstance(index)
        }
    }
}
