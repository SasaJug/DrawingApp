package com.sasaj.graphics.drawingapp.ui.drawing

import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.*
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.domain.Brush
import com.sasaj.graphics.drawingapp.system.init
import com.sasaj.graphics.drawingapp.system.setBrush
import com.sasaj.graphics.drawingapp.viewmodel.DrawingViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_drawing.*

class DrawingFragment : Fragment() {

    private var vm: DrawingViewModel? = null
    private val paint: Paint = Paint()
    private lateinit var disposable : Disposable;


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
       disposable = vm!!.getBrush().observeOn(AndroidSchedulers.mainThread())
                .subscribe { brush: Brush? ->
                    paint.setBrush(brush!!)
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


    private fun startToolsDialog() {
        val newFragment = SelectBrushDialogFragment.newInstance()
        newFragment.show(fragmentManager!!, "com.sasaj.graphics.drawingapp.dialog")
    }

    private fun startSaveDialog() {
        val alertDialogBuilder = AlertDialog.Builder(activity!!)

        alertDialogBuilder.setTitle(R.string.app_title)

        alertDialogBuilder
                .setMessage(R.string.save_drawing_question)
                .setCancelable(false)
                .setPositiveButton(R.string.ok) { dialogInterface, i ->
                    saveDrawing()
                    dialogInterface.cancel()
                }
                .setNegativeButton(R.string.cancel) { dialogInterface, i -> dialogInterface.cancel() }.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
    }

    private fun saveDrawing() {
        vm?.saveDrawing(bitmap)
    }

}
