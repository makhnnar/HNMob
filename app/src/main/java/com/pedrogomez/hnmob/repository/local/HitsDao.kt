package com.pedrogomez.hnmob.repository.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedrogomez.hnmob.models.db.HitTable

@Dao
interface HitsDao {

    @Query("SELECT * FROM hit_table")
    fun observeHits(): LiveData<List<HitTable>>

    @Query(value = "SELECT * from hit_table WHERE hit_table.isDeleted='false'")
    fun getAllHits(): List<HitTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(hitTable: HitTable)

    @Query(value = "UPDATE hit_table SET isDeleted='true' WHERE hit_table.objectID=:objectID")
    fun delete(objectID:String)

}