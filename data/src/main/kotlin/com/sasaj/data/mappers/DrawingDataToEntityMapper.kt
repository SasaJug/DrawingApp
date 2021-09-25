package com.sasaj.data.mappers

import android.util.Log
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
       Log.e("map", "drawings: $list")
       val finalList = mutableListOf<Drawing>()
        list.forEach{drawingDb -> finalList.add(mapFrom(drawingDb))}
        return finalList
    }
}