package com.sasaj.graphics.drawingapp.mappers

import com.sasaj.domain.common.Mapper
import com.sasaj.domain.entities.Drawing
import com.sasaj.graphics.drawingapp.entities.DrawingUI

class DrawingEntityToUIMapper : Mapper<Drawing, DrawingUI>() {
    override fun mapFrom(from: Drawing): DrawingUI {
        return DrawingUI(from.id,
                from.fileName,
                from.imagePath,
                from.lastModified)
    }
}