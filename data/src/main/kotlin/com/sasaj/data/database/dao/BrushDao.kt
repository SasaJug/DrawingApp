package com.sasaj.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.sasaj.data.entities.BrushDb

@Dao
interface BrushDao {

    @Query("SELECT * FROM brush order by id asc limit 1")
    fun getLastSaved(): BrushDb?

    @Insert(onConflict = REPLACE)
    fun insert(brushDb: BrushDb) : Long

    @Query("DELETE from brush")
    fun deleteAll()
}