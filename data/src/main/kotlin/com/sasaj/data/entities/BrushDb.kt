package com.sasaj.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.sasaj.data.database.*

@Entity(tableName = BRUSH_TABLE_NAME)
internal data class BrushDb(@PrimaryKey val id: Long,
                   @ColumnInfo(name = SIZE_COLUMN_NAME) var size: Int,
                   @ColumnInfo(name = BLUR_COLUMN_NAME) var blur: Float,
                   @ColumnInfo(name = COLOR_COLUMN_NAME) var color: Int) {

    // id will always be 0 if this constructor is used, which means that only one brush can be saved in the database.
    // This will be changed when presets selector is implemented.
    // ToDo - implement saving multiple brushes in the database.
    @Ignore
    constructor(size: Int = 5, blur: Float = 0f, color: Int = 0xff000000.toInt()) : this(0, size, blur, color)
}