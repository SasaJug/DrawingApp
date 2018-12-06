package com.sasaj.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.sasaj.data.entities.DrawingDb
import io.reactivex.Flowable
import io.reactivex.Observable

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