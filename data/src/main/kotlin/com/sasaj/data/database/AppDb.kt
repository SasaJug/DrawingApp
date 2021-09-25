package com.sasaj.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sasaj.data.database.dao.BrushDao
import com.sasaj.data.database.dao.DrawingDao
import com.sasaj.data.entities.BrushDb
import com.sasaj.data.entities.DrawingDb

@Database(entities = [(DrawingDb::class), (BrushDb::class)], version = 4, exportSchema = false)
internal abstract class AppDb : RoomDatabase() {
    abstract fun brushDao(): BrushDao
    abstract fun drawingDao(): DrawingDao
}