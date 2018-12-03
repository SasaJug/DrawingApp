package com.sasaj.graphics.drawingapp.viewmodel

import com.sasaj.graphics.drawingapp.domain.Brush
import com.sasaj.graphics.drawingapp.viewmodel.dependencies.BrushRepository
import javax.inject.Inject

class SelectBrushViewModel : BaseViewModel() {

    @Inject
    lateinit var brushRepository: BrushRepository


    fun getCurrentBrush(): Brush {
        return brushRepository.getCurrentBrush()
    }

    fun setBrush(brush: Brush) {
        brushRepository.setCurrentBrush(brush)
    }

}