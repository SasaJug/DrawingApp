package com.sasaj.data.repositories

import com.sasaj.data.database.AppDb
import javax.inject.Inject

internal class LocalRepository @Inject constructor(
    private val db: AppDb
) {

    fun deleteAll() {
        db.brushDao().deleteAll()
        db.drawingDao().deleteAll()
        db.clearAllTables()
    }
}