package com.sasaj.graphics.drawingapp.mappers

import com.sasaj.domain.common.Mapper
import com.sasaj.domain.entities.Brush
import com.sasaj.graphics.drawingapp.entities.BrushUI

class BrushEntityToUIMapper() : Mapper<Brush, BrushUI>() {
    override fun mapFrom(from: Brush): BrushUI {
        return BrushUI(from.size,
                from.blur,
                from.color)
    }
}