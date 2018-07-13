package com.sasaj.graphics.drawingapp.repository.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.sasaj.graphics.drawingapp.domain.Brush
import com.sasaj.graphics.drawingapp.repository.database.dao.BrushDao

@Database(entities = arrayOf(Brush::class), version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun brushDao(): BrushDao
}