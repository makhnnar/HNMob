package com.pedrogomez.hnmob.repository.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pedrogomez.hnmob.models.db.HitTable

@Dao
interface HitsDao {

    @Query("SELECT * FROM hit_table")
    fun getAllHits(): List<HitTable>

    @Query(value = "SELECT * from hit_table WHERE hit_table.isDeleted=:filter")
    fun observeHits(filter:Boolean = false): LiveData<List<HitTable>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(hitTable: HitTable)

    //@Query(value = "UPDATE hit_table SET isDeleted='true' WHERE hit_table.objectID=:objectID")
    @Update
    fun delete(
            //objectID:String
            hitTable: HitTable
    ):Int

}