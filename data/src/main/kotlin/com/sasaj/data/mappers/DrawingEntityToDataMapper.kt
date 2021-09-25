package com.sasaj.data.mappers

import com.sasaj.data.entities.DrawingDb
import com.sasaj.domain.common.Mapper
import com.sasaj.domain.entities.Drawing

internal class DrawingEntityToDataMapper() : Mapper<Drawing, DrawingDb>() {
    override fun mapFrom(from: Drawing): DrawingDb {
        return DrawingDb(
                from.fileName,
                from.imagePath,
                from.lastModified)
    }
}