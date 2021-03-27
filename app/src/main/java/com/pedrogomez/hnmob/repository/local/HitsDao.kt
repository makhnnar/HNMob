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

    @Query("SELECT * from hit_table")
    fun getAllHits(): List<HitTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(hitTable: HitTable)

}