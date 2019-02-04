package com.sasaj.data.repositories

import com.sasaj.data.database.AppDb

class LocalRepository(private val db: AppDb) {

    fun deleteAll() {
        db.brushDao().deleteAll()
        db.drawingDao().deleteAll()
        db.clearAllTables()
    }
}