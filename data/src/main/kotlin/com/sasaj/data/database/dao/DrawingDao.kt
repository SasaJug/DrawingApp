package com.sasaj.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sasaj.data.entities.DrawingDb
import io.reactivex.Flowable

@Dao
interface DrawingDao {

    @Query("SELECT * from drawing")
    fun getAll(): Flowable<List<DrawingDb>>

    @Query("SELECT * FROM drawing where id = :drawingId")
    fun getById(drawingId: Long): Flowable<List<DrawingDb>>


    @Query("SELECT * FROM drawing where filename = :filename")
    fun getByFilename(filename: String?): DrawingDb?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(drawingDb: DrawingDb) : Long

    @Query("DELETE from drawing")
    fun deleteAll()
}