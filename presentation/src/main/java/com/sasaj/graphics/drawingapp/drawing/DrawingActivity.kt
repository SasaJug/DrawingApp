package com.sasaj.graphics.drawingapp.drawing

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.common.BaseActivity
import com.sasaj.graphics.drawingapp.drawing.brushselector.utilities.BrushAdapter.brush
import kotlinx.android.synthetic.main.activity_drawing.*

class DrawingActivity : BaseActivity(){

    private lateinit var vmDrawing: DrawingViewModel
    private lateinit var vmNavigation: DrawingNavigationViewModel

    companion object {
        val TAG = DrawingActivity::class.java.simpleName
        const val DRAWING_FRAGMENT_TAG = "drawingFragment"
        const val DIALOG_FRAGMENT_TAG = "dialogFragment"

        const val ORIENTATION = "ORIENTATION"
        const val LANDSCAPE = 0
        const val PORTRAIT = 1


        fun createIntent(context: Context, orientation: Int): Intent {
            val intent = Intent(context, DrawingActivity::class.java)
            intent.putExtra(ORIENTATION, orientation)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setScreenOrientation(intent.getIntExtra(ORIENTATION, PORTRAIT))
        setContentView(R.layout.activity_drawing)
        setSupportActionBar(toolbar_drawing)

        vmDrawing = ViewModelProviders.of(this).get(DrawingViewModel::class.java)
        vmNavigation = ViewModelProviders.of(this).get(DrawingNavigationViewModel::class.java)

        vmDrawing.drawingLiveData.observe(this, Observer {
            if (it != null) handleViewState(it)
        })


        vmNavigation.drawingNavigationLiveData.observe(this, Observer {
            if (it != null) handleNavigationState(it)
        })

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, DrawingFragment(), DRAWING_FRAGMENT_TAG)
                    .commitNow()
        }

        vmDrawing.getLastBrush()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_drawing, menu)
        return true
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
        val currentBrush = (supportFragmentManager.findFragmentByTag(DRAWING_FRAGMENT_TAG) as DrawingFragment).currentBrushUI
        val dialogFragment = SelectBrushDialogFragment.newInstance(currentBrush)
        dialogFragment.showNow(supportFragmentManager, DIALOG_FRAGMENT_TAG)
    }

    private fun startSaveDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)

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
        vmDrawing?.saveDrawing((supportFragmentManager.findFragmentByTag(DRAWING_FRAGMENT_TAG) as DrawingFragment).bitmap)
    }


    private fun handleViewState(drawingViewState: DrawingViewState) {
        when {
            drawingViewState.initialized.not() -> return
            drawingViewState.loading -> showProgress()
            drawingViewState.brush != null -> {
                val localBrush = drawingViewState.brush
                hideProgress()
                vmNavigation.drawingNavigationLiveData.value = DrawingNavigationViewState(brushUI = localBrush)
            }
            drawingViewState.brushSaved-> {
                hideProgress()
                Log.i(TAG, "Brush saved successfully")
            }
            drawingViewState.bitmapSaved -> {
                hideProgress()
                Log.i(TAG, "Bitmap saved successfully")
                finish()
            }
        }
    }


    private fun handleNavigationState(navigationViewState: DrawingNavigationViewState) {
        navigationViewState.brushUI?.let {
            vmDrawing.saveBrush(navigationViewState.brushUI!!)
        }
    }


    private fun setScreenOrientation(orientation: Int) {
        requestedOrientation = if (orientation == PORTRAIT) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }
}

