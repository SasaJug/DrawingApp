package com.sasaj.graphics.drawingapp.mappers

import com.sasaj.domain.common.Mapper
import com.sasaj.domain.entities.Brush
import com.sasaj.graphics.drawingapp.entities.BrushUI

class BrushUIToEntityMapper() : Mapper<BrushUI, Brush>() {
    override fun mapFrom(from: BrushUI): Brush {
        return Brush(0,
                from.size,
                from.blur,
                from.color)
    }
}