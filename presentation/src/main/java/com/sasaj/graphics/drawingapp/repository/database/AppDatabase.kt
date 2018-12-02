package com.sasaj.graphics.drawingapp.repository.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.sasaj.graphics.drawingapp.domain.Brush
import com.sasaj.graphics.drawingapp.domain.Drawing
import com.sasaj.graphics.drawingapp.repository.database.dao.BrushDao
import com.sasaj.graphics.drawingapp.repository.database.dao.DrawingDao

@Database(entities = [(Drawing::class), (Brush::class)], version = 7, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun brushDao(): BrushDao
    abstract fun drawingDao(): DrawingDao
}