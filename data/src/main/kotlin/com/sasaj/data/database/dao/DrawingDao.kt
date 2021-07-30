package com.sasaj.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sasaj.data.entities.DrawingDb
import io.reactivex.Flowable

@Dao
interface DrawingDao {

    @Query("SELECT * FROM drawing")
    fun getAll(): Flowable<List<DrawingDb>>

    @Query("SELECT * FROM drawing WHERE id = :drawingId")
    fun getById(drawingId: Long): Flowable<List<DrawingDb>>

    @Query("SELECT * FROM drawing WHERE filename = :filename")
    fun getByFilename(filename: String?): DrawingDb?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(drawingDb: DrawingDb) : Long

    @Query("DELETE FROM drawing")
    fun deleteAll()
}