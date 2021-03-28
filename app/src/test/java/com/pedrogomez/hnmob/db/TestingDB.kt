package com.pedrogomez.hnmob.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pedrogomez.hnmob.repository.local.HitsDao
import com.pedrogomez.hnmob.repository.local.HitsDataBase
import com.pedrogomez.hnmob.util.DataHelper
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestingDB {

    private lateinit var hitsDao: HitsDao
    private lateinit var db: HitsDataBase

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun createDb() {
        Dispatchers.setMain(mainThreadSurrogate)
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            HitsDataBase::class.java
        ).build()
        hitsDao = db.hits()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeAndRedHits() {
        runBlocking {
            launch(Dispatchers.Main) {
                val hitTable = DataHelper.HITTABLE
                hitsDao.insert(hitTable)
                val hits = hitsDao.getAllHits()
                Assert.assertEquals(hits,DataHelper.HITSLIST)
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun writeAndDeleteHitWithReturnValue() {
        runBlocking {
            launch(Dispatchers.Main) {
                val hitTable = DataHelper.HITTABLE
                hitsDao.insert(hitTable)
                hitTable.isDeleted = true
                val result = hitsDao.delete(hitTable)
                Assert.assertEquals(result,1)
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun writeAndDeleteHitCheckEmptyListSuccess() {
        runBlocking {
            launch(Dispatchers.Main) {
                val hitTable = DataHelper.HITTABLE
                hitsDao.insert(hitTable)
                hitTable.isDeleted = true
                hitsDao.delete(hitTable)
                val hits = hitsDao.getAllHits()
                Assert.assertEquals(hits,DataHelper.EMPTYHISTS)
            }
        }
    }

}