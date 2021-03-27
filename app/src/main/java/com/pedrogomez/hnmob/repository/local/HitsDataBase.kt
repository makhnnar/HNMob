package com.pedrogomez.hnmob.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pedrogomez.hnmob.models.db.HitTable

@Database(entities = [HitTable::class], version = 2)
abstract class HitsDataBase : RoomDatabase() {

        abstract fun hits(): HitsDao

}