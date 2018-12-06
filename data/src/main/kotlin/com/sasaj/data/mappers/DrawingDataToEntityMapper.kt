package com.sasaj.data.mappers

import com.sasaj.data.entities.DrawingDb
import com.sasaj.domain.common.Mapper
import com.sasaj.domain.entities.Drawing

class DrawingDataToEntityMapper() : Mapper<DrawingDb, Drawing>() {
    override fun mapFrom(from: DrawingDb): Drawing {
        return Drawing(from.id,
                from.fileName,
                from.imagePath,
                from.lastModified)
    }
}