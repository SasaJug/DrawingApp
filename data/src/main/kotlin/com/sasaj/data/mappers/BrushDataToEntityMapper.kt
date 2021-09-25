package com.sasaj.data.mappers

import com.sasaj.data.entities.BrushDb
import com.sasaj.domain.common.Mapper
import com.sasaj.domain.entities.Brush

internal class BrushDataToEntityMapper() : Mapper<BrushDb, Brush>() {
    override fun mapFrom(from: BrushDb): Brush {
        return Brush(from.id,
                from.size,
                from.blur,
                from.color)
    }
}