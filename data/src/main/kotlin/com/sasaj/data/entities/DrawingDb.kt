package com.sasaj.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.sasaj.data.database.*

/**
 * Created by sjugurdzija on 4/21/2017.
 */

@Entity(tableName = DRAWING_TABLE_NAME)
internal data class DrawingDb(@PrimaryKey(autoGenerate = true) val id: Long?,
                @ColumnInfo(name = FILENAME_COLUMN_NAME) val fileName: String,
                @ColumnInfo(name = PATH_COLUMN_NAME) val imagePath: String,
                @ColumnInfo(name = LAST_MODIFIED_COLUMN_NAME) val lastModified: Long) : Comparable<DrawingDb> {

    @Ignore
    constructor(key : String = "", imagePath : String = "", lastModified: Long = 0) : this(null, key, imagePath, lastModified)

    override fun compareTo(other: DrawingDb): Int {
        val compareQuantity = other.lastModified
        return (compareQuantity - this.lastModified).toInt()
    }
}
