package com.sasaj.data.mappers

import com.sasaj.data.entities.BrushDb
import com.sasaj.domain.common.Mapper
import com.sasaj.domain.entities.Brush

internal class BrushEntityToDataMapper() : Mapper<Brush, BrushDb>() {
    override fun mapFrom(from: Brush): BrushDb {
        return BrushDb(from.size,
                from.blur,
                from.color)
    }
}