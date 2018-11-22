package com.sasaj.graphics.drawingapp.repository.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.sasaj.graphics.drawingapp.domain.Drawing
import io.reactivex.Flowable

@Dao
interface DrawingDao {

    @Query("SELECT * from drawing")
    fun getAll(): Flowable<List<Drawing>>

    @Query("SELECT * FROM drawing where id = :drawingId")
    fun getById(drawingId: Long): Flowable<List<Drawing>>


    @Query("SELECT * FROM drawing where filename = :filename")
    fun getByFilename(filename: String?): Drawing?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(drawing: Drawing)

    @Query("DELETE from drawing")
    fun deleteAll()
}