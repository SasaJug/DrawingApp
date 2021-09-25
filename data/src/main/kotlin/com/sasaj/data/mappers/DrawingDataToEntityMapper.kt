package com.sasaj.data.mappers

import com.sasaj.data.entities.DrawingDb
import com.sasaj.domain.common.Mapper
import com.sasaj.domain.entities.Drawing

internal class DrawingDataToEntityMapper() : Mapper<DrawingDb, Drawing>() {
    override fun mapFrom(from: DrawingDb): Drawing {
        return Drawing(from.id,
                from.fileName,
                from.imagePath,
                from.lastModified)
    }


   fun mapFromList(list : List<DrawingDb>) : List<Drawing>{
        val finalList = mutableListOf<Drawing>()
        list.forEach{drawingDb -> finalList.add(mapFrom(drawingDb))}
        return finalList
    }
}