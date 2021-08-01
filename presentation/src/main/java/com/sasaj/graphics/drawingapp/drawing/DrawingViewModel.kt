package com.sasaj.graphics.drawingapp.drawing

import androidx.lifecycle.MutableLiveData
import android.graphics.Bitmap
import android.util.Log
import com.sasaj.domain.entities.Brush
import com.sasaj.domain.entities.Optional
import com.sasaj.domain.usecases.GetBrush
import com.sasaj.domain.usecases.SaveBrush
import com.sasaj.domain.usecases.SaveDrawing
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.BitmapManager
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import com.sasaj.graphics.drawingapp.common.UIException
import com.sasaj.graphics.drawingapp.entities.BrushUI
import com.sasaj.graphics.drawingapp.mappers.BrushEntityToUIMapper
import com.sasaj.graphics.drawingapp.mappers.BrushUIToEntityMapper

class DrawingViewModel(private val saveDrawing: SaveDrawing,
                       private val getBrush: GetBrush,
                       private val saveBrush: SaveBrush) : BaseViewModel() {

    val drawingLiveData: MutableLiveData<DrawingViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<UIException?> = SingleLiveEvent()

    init {
        drawingLiveData.value = DrawingViewState()
    }


    fun saveDrawing(bitmap: Bitmap?) {
        drawingLiveData.value = drawingLiveData.value?.copy(initialized = true, loading = true, brush = null, brushSaved = false, bitmapSaved = false)
        val bitmapManager = BitmapManager()
        bitmapManager.bitmap = bitmap
        addDisposable(saveDrawing.saveDrawing(bitmapManager)
                .subscribe(
                        { s: Boolean ->
                            val newDrawingViewState = drawingLiveData.value?.copy(loading = false, bitmapSaved = true)
                            drawingLiveData.value = newDrawingViewState
                            errorState.value = null
                        },
                        { e ->
                            drawingLiveData.value = drawingLiveData.value?.copy(loading = false, bitmapSaved = false)
                            errorState.value = UIException(cause = e)
                        },
                        { Log.i(TAG, "Drawing saved") }
                )
        )
    }


    fun getLastBrush() {
        drawingLiveData.value = drawingLiveData.value?.copy(initialized = true, loading = true, brush = null, brushSaved = false, bitmapSaved = false)
        addDisposable(getBrush.getLastBrush()
                .subscribe(
                        { s: Optional<Brush> ->

                            when {
                                s.value != null ->{
                                    val brushUI: BrushUI = BrushEntityToUIMapper().mapFrom(s.value!!)
                                    val newDrawingViewState = drawingLiveData.value?.copy(loading = false, brush = brushUI)
                                    drawingLiveData.value = newDrawingViewState
                                }
                                else -> {
                                    val newDrawingViewState = drawingLiveData.value?.copy(loading = false, brush = null)
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
        drawingLiveData.value = drawingLiveData.value?.copy(initialized = true, loading = true, brush = null, brushSaved = false, bitmapSaved = false)
        addDisposable(saveBrush.saveBrush(BrushUIToEntityMapper().mapFrom(brushUI))
                .subscribe(
                        { s: Boolean ->
                            val newDrawingViewState = drawingLiveData.value?.copy(loading = false, brushSaved = true)
                            drawingLiveData.value = newDrawingViewState
                            errorState.value = null
                        },
                        { e ->
                            drawingLiveData.value = drawingLiveData.value?.copy(loading = false, brushSaved = false)
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