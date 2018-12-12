package com.sasaj.graphics.drawingapp.drawing

import android.graphics.Bitmap
import com.sasaj.domain.entities.Optional
import com.sasaj.domain.usecases.GetBrush
import com.sasaj.domain.usecases.SaveBrush
import com.sasaj.domain.usecases.SaveDrawing
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.BitmapManager
import io.reactivex.Observable
import javax.inject.Inject

class DrawingViewModel : BaseViewModel() {

    @Inject
    lateinit var saveDrawing: SaveDrawing

    @Inject
    lateinit var getBrush: GetBrush

    @Inject
    lateinit var saveBrush: SaveBrush

    fun saveDrawing(bitmap: Bitmap?) {
            val bitmapManager = BitmapManager()
            bitmapManager.bitmap = bitmap
            saveDrawing.saveDrawing(bitmapManager).subscribe()
    }

    fun getLastBrush() : Observable<Optional<com.sasaj.domain.entities.Brush>> {
        return getBrush.getLastBrush()
    }

    fun saveBrush(brush: com.sasaj.domain.entities.Brush) : Observable<Boolean>  {
        return saveBrush.saveBrush(brush)
    }
}