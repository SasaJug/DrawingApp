package com.sasaj.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
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