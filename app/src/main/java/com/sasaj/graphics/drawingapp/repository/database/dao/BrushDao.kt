package com.sasaj.graphics.drawingapp.repository.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.sasaj.graphics.drawingapp.domain.Brush
import io.reactivex.Flowable

@Dao
interface BrushDao {

    @Query("SELECT * from brush")
    fun getAll(): Flowable<List<Brush>>

    @Query("SELECT * FROM brush where id = :brushId")
    fun getById(brushId: Long): Flowable<List<Brush>>

    @Insert(onConflict = REPLACE)
    fun insert(brush: Brush)

    @Query("DELETE from brush")
    fun deleteAll()
}