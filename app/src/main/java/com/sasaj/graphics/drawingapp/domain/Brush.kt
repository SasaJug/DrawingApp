package com.sasaj.graphics.drawingapp.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.sasaj.graphics.drawingapp.repository.database.BLUR_COLUMN_NAME
import com.sasaj.graphics.drawingapp.repository.database.BRUSH_TABLE_NAME
import com.sasaj.graphics.drawingapp.repository.database.COLOR_COLUMN_NAME
import com.sasaj.graphics.drawingapp.repository.database.SIZE_COLUMN_NAME

@Entity(tableName = BRUSH_TABLE_NAME)
data class Brush(@PrimaryKey(autoGenerate = true) val id: Long,
                 @ColumnInfo(name = SIZE_COLUMN_NAME) val size: Int,
                 @ColumnInfo(name = BLUR_COLUMN_NAME) val blur: Float,
                 @ColumnInfo(name = COLOR_COLUMN_NAME) val color: Int){
}