package com.sasaj.graphics.drawingapp.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.sasaj.graphics.drawingapp.repository.database.*

/**
 * Created by sjugurdzija on 4/21/2017.
 */

@Entity(tableName = DRAWING_TABLE_NAME)
class Drawing(@PrimaryKey(autoGenerate = true) val id: Long?,
              @ColumnInfo(name = FILENAME_COLUMN_NAME) val fileName: String,
              @ColumnInfo(name = PATH_COLUMN_NAME) val imagePath: String,
              @ColumnInfo(name = LAST_MODIFIED_COLUMN_NAME) val lastModified: Long) : Comparable<Drawing> {

    @Ignore
    constructor(key : String = "", imagePath : String = "", lastModified: Long = 0) : this(null, key, imagePath, lastModified)

    override fun compareTo(other: Drawing): Int {
        val compareQuantity = other.lastModified
        return (compareQuantity - this.lastModified).toInt()
    }
}
