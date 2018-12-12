package com.sasaj.graphics.drawingapp.drawing


import android.app.Activity.RESULT_OK
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.*
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.entities.BrushUI
import com.sasaj.graphics.drawingapp.mappers.BrushEntityToUIMapper
import com.sasaj.graphics.drawingapp.mappers.BrushUIToEntityMapper
import com.sasaj.graphics.drawingapp.common.init
import com.sasaj.graphics.drawingapp.common.setPaintParameters
import com.sasaj.graphics.drawingapp.drawing.SelectBrushDialogFragment.Companion.BRUSH_PARCELABLE
import com.sasaj.graphics.drawingapp.drawing.SelectBrushDialogFragment.Companion.BRUSH_REQUEST_CODE
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_drawing.*

class DrawingFragment : Fragment() {

    private var vm: DrawingViewModel? = null
    private val paint: Paint = Paint()
    private var brushUI : BrushUI = BrushUI()
    private lateinit var disposable: Disposable

    private val bitmap: Bitmap?
        get() = drawing!!.bitmapFromView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        vm = ViewModelProviders.of(this).get(DrawingViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_drawing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paint.init()
        drawing?.setPaint(paint)
    }

    override fun onResume() {
        super.onResume()
        disposable = vm!!.getLastBrush()
                .subscribe { optional ->
                    if (optional.hasValue()) {
                        brushUI = BrushEntityToUIMapper().mapFrom(optional.value!!)
                    }
                    paint.setPaintParameters(brushUI)
                    drawing?.setPaint(paint)
                }
    }

    override fun onCreateOptionsMenu(
            menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_fragment_drawing, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.action_choose_tool -> {
                startToolsDialog()
                true
            }
            R.id.action_save_drawing -> {
                startSaveDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK && requestCode == BRUSH_REQUEST_CODE){
            brushUI = data!!.getParcelableExtra(BRUSH_PARCELABLE)
            paint.setPaintParameters(brushUI)
            drawing?.setPaint(paint)
            vm!!.saveBrush(BrushUIToEntityMapper().mapFrom(brushUI)).subscribe()
        }
    }

    private fun startToolsDialog() {
        val newFragment = SelectBrushDialogFragment.newInstance(this, brushUI)
        newFragment.show(fragmentManager!!, "com.sasaj.graphics.drawingapp.dialog")
    }

    private fun startSaveDialog() {
        val alertDialogBuilder = AlertDialog.Builder(activity!!)

        alertDialogBuilder.setTitle(R.string.app_title)

        alertDialogBuilder
                .setMessage(R.string.save_drawing_question)
                .setCancelable(false)
                .setPositiveButton(R.string.ok) { dialogInterface, _ ->
                    saveDrawing()
                    dialogInterface.cancel()
                }
                .setNegativeButton(R.string.cancel) { dialogInterface, i -> dialogInterface.cancel() }.show()
    }

    private fun saveDrawing() {
        vm?.saveDrawing(bitmap)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
    }
}
