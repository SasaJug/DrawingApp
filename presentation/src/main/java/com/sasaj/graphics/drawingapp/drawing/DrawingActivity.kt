package com.sasaj.graphics.drawingapp.drawing

import androidx.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import com.sasaj.domain.entities.Brush
import com.sasaj.domain.entities.Optional
import com.sasaj.domain.usecases.GetBrush
import com.sasaj.domain.usecases.SaveBrush
import com.sasaj.domain.usecases.SaveDrawing
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.common.*
import com.sasaj.graphics.drawingapp.entities.BrushUI
import com.sasaj.graphics.drawingapp.mappers.BrushEntityToUIMapper
import com.sasaj.graphics.drawingapp.mappers.BrushUIToEntityMapper
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.synthetic.main.activity_drawing.*
import javax.inject.Inject

@AndroidEntryPoint
class DrawingActivity : BaseActivity() {
//
//    @Inject
//    lateinit var drawingVMFactory: DrawingVMFactory

    private val vmDrawing by viewModels<DrawingViewModel>()
    private val vmNavigation by viewModels<DrawingNavigationViewModel>()

    //region activity callbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        (application as DrawingApplication).createMainComponent().inject(this)

        setScreenOrientation(intent.getIntExtra(ORIENTATION, PORTRAIT))
        setContentView(R.layout.activity_drawing)
        setSupportActionBar(toolbar_drawing)

//        vmDrawing = ViewModelProviders.of(this, drawingVMFactory).get(DrawingViewModel::class.java)
//        vmNavigation = ViewModelProviders.of(this).get(DrawingNavigationViewModel::class.java)

        vmDrawing.drawingLiveData.observe(this, Observer {
            if (it != null) handleViewState(it)
        })

        vmDrawing.errorState.observe(this, Observer {
            if (it != null) handleError(it)
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
//
//    override fun onDestroy() {
//        (application as DrawingApplication).releaseMainComponent()
//        super.onDestroy()
//    }

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
    //endregion

    //region view state and error handlers
    private fun handleViewState(drawingViewState: DrawingViewState) {
        showProgress(false)
        when {
            drawingViewState.initialized.not() -> return
            drawingViewState.loading -> showProgress(true)
            drawingViewState.brush != null -> {
                val localBrush = drawingViewState.brush
                hideProgress()
                vmNavigation.drawingNavigationLiveData.value = DrawingNavigationViewState(brushUI = localBrush)
            }
            drawingViewState.brushSaved -> {
                showProgress(false)
                Log.i(TAG, "Brush saved successfully")
            }
            drawingViewState.bitmapSaved -> {
                showProgress(false)
                Log.i(TAG, "Bitmap saved successfully")
                finish()
            }
        }
    }

    private fun handleError(uiException: UIException?) {
        showProgress(false)
        showDialogMessage("Error ", uiException!!.cause.toString())
    }

    private fun handleNavigationState(navigationViewState: DrawingNavigationViewState) {
        navigationViewState.brushUI?.let {
            vmDrawing.saveBrush(navigationViewState.brushUI!!)
        }
    }
    //endregion

    //region rendering

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
        vmDrawing.saveDrawing((supportFragmentManager.findFragmentByTag(DRAWING_FRAGMENT_TAG) as DrawingFragment).bitmap)
    }
    private fun showProgress(show: Boolean) {
        if (show)
            progressDrawingActivity.visibility = View.VISIBLE
        else
            progressDrawingActivity.visibility = View.GONE
    }

    private fun setScreenOrientation(orientation: Int) {
        requestedOrientation = if (orientation == PORTRAIT) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }
    //endregion

    companion object {
        val TAG: String = DrawingActivity::class.java.simpleName
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
}

@HiltViewModel
class DrawingViewModel @Inject constructor(
    private val saveDrawing: SaveDrawing,
    private val getBrush: GetBrush,
    private val saveBrush: SaveBrush
) : BaseViewModel() {

    val drawingLiveData: MutableLiveData<DrawingViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<UIException?> = SingleLiveEvent()

    init {
        drawingLiveData.value = DrawingViewState()
    }


    fun saveDrawing(bitmap: Bitmap?) {
        drawingLiveData.value = drawingLiveData.value?.copy(
            initialized = true,
            loading = true,
            brush = null,
            brushSaved = false,
            bitmapSaved = false
        )
        val bitmapManager = BitmapManager()
        bitmapManager.bitmap = bitmap
        addDisposable(saveDrawing.saveDrawing(bitmapManager)
            .subscribe(
                { s: Boolean ->
                    val newDrawingViewState =
                        drawingLiveData.value?.copy(loading = false, bitmapSaved = true)
                    drawingLiveData.value = newDrawingViewState
                    errorState.value = null
                },
                { e ->
                    drawingLiveData.value =
                        drawingLiveData.value?.copy(loading = false, bitmapSaved = false)
                    errorState.value = UIException(cause = e)
                },
                { Log.i(TAG, "Drawing saved") }
            )
        )
    }


    fun getLastBrush() {
        drawingLiveData.value = drawingLiveData.value?.copy(
            initialized = true,
            loading = true,
            brush = null,
            brushSaved = false,
            bitmapSaved = false
        )
        addDisposable(getBrush.getLastBrush()
            .subscribe(
                { s: Optional<Brush> ->

                    when {
                        s.value != null -> {
                            val brushUI: BrushUI = BrushEntityToUIMapper().mapFrom(s.value!!)
                            val newDrawingViewState =
                                drawingLiveData.value?.copy(loading = false, brush = brushUI)
                            drawingLiveData.value = newDrawingViewState
                        }
                        else -> {
                            val newDrawingViewState =
                                drawingLiveData.value?.copy(loading = false, brush = null)
                            drawingLiveData.value = newDrawingViewState
                        }
                    }
                },
                { e ->
                    errorState.value = UIException(cause = e)
                },
                { Log.i(TAG, "Brush retrieved") }
            )
        )
    }


    fun saveBrush(brushUI: BrushUI) {
        drawingLiveData.value = drawingLiveData.value?.copy(
            initialized = true,
            loading = true,
            brush = null,
            brushSaved = false,
            bitmapSaved = false
        )
        addDisposable(saveBrush.saveBrush(BrushUIToEntityMapper().mapFrom(brushUI))
            .subscribe(
                { s: Boolean ->
                    val newDrawingViewState =
                        drawingLiveData.value?.copy(loading = false, brushSaved = true)
                    drawingLiveData.value = newDrawingViewState
                    errorState.value = null
                },
                { e ->
                    drawingLiveData.value =
                        drawingLiveData.value?.copy(loading = false, brushSaved = false)
                    errorState.value = UIException(cause = e)
                },
                { Log.i(TAG, "Brush saved") }
            )
        )
    }

    companion object {
        private val TAG = DrawingViewModel::class.java.simpleName
    }
}

