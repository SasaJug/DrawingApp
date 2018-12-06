package com.sasaj.graphics.drawingapp.mappers

import com.sasaj.domain.common.Mapper
import com.sasaj.domain.entities.Drawing
import com.sasaj.graphics.drawingapp.entities.DrawingUI

class DrawingUIToEntityMapper : Mapper<DrawingUI, Drawing>(){
    override fun mapFrom(from: DrawingUI): Drawing {
        return Drawing(from.id,
                from.fileName,
                from.imagePath,
                from.lastModified)
    }
}