package com.pedrogomez.hnmob.unittest.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pedrogomez.hnmob.models.api.HitsListResponse
import com.pedrogomez.hnmob.models.api.toPresentationModel
import com.pedrogomez.hnmob.models.db.HitTable
import com.pedrogomez.hnmob.repository.HitsProvider
import com.pedrogomez.hnmob.unittest.util.DataHelper
import com.pedrogomez.hnmob.unittest.util.getOrAwaitValue
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

import org.junit.After
import org.junit.Assert.*

//@RunWith(AndroidJUnit4::class)
class HitsProviderTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private lateinit var SUT: HitsProvider

    val PAGE = 1

    var remoteDataSourceTD = RemoteDataSourceTD()
    var localDataSourceTD =  LocalDataSourceTD()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        SUT = HitsProvider(
            remoteDataSourceTD,
            localDataSourceTD
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun onLoadHits_getHitsData_passedParamsSuccess() {
        runBlocking {
            launch(Dispatchers.Main) {
                SUT.loadHits(PAGE)
                remoteDataSourceTD.getHitsData(PAGE)
                assertEquals(remoteDataSourceTD.page,PAGE)
            }
        }
    }

    @Test
    fun onLoadHits_getHitsData_successResponse() {
        runBlocking {
            launch(Dispatchers.Main) {
                SUT.loadHits(PAGE)
                val response = remoteDataSourceTD.getHitsData(PAGE)
                assertEquals(response, DataHelper.HITSRESPONSE)
            }
        }
    }

    @Test
    fun onLoadHits_getHitsData_failedResponse() {
        failed()
        runBlocking {
            launch(Dispatchers.Main) {
                SUT.loadHits(PAGE)
                val response = remoteDataSourceTD.getHitsData(PAGE)
                assertNull(response)
            }
        }
    }

    @Test
    fun getAllHits_getAllHits_returnedSuccess() {
        runBlocking {
            launch(Dispatchers.Main) {
                val list = SUT.getAllHits()
                assertEquals(
                        list,
                        DataHelper.HITSLIST
                )
            }
        }
    }

    @Test
    fun delete_delete_passedParamsSuccess() {
        runBlocking {
            launch(Dispatchers.Main) {
                SUT.delete(DataHelper.HITTABLE)
                assertEquals(
                        localDataSourceTD.hitToDelete,
                        DataHelper.HITTABLE
                )
            }
        }
    }

    @Test
    fun observeHits_observeHits_returnedSuccess() {
        runBlocking {
            onDataChange()
            val list = SUT.observeHits()
            assertEquals(
                list.getOrAwaitValue(),
                DataHelper.HITSLIST
            )
        }
    }

    @Test
    fun updateLocalWithRemote_insert_passedParamsSuccess() {
        runBlocking {
            launch(Dispatchers.Main) {
                SUT.updateLocalWithRemote(
                        DataHelper.HITSRESPONSE.hits.map {
                             it.toPresentationModel()
                         }
                )
                assertEquals(
                        localDataSourceTD.listHits,
                        DataHelper.HITSRESPONSE.hits.map {
                            it.toPresentationModel()
                        }
                )
            }
        }
    }

    private fun failed() {
        remoteDataSourceTD.failed = true
    }

    private fun onDataChange() {
        localDataSourceTD.hitsMutLivDat.postValue(
                DataHelper.HITSLIST
        )
    }

    class RemoteDataSourceTD : HitsProvider.RemoteDataSource {

        var page = 0

        var failed = false

        override suspend fun getHitsData(page: Int): HitsListResponse? {
            this.page = page
            return if(failed) null else DataHelper.HITSRESPONSE
        }

    }

    class LocalDataSourceTD : HitsProvider.LocalDataSource{

        var hitToDelete : HitTable? = null

        var hitsMutLivDat = MutableLiveData<List<HitTable>>()

        var inserted : HitTable? = null

        var listHits = ArrayList<HitTable>()

        override suspend fun getAllHits(): List<HitTable> {
            return DataHelper.HITSLIST
        }

        override suspend fun insert(hitTable: HitTable) {
            inserted = hitTable
        }

        override suspend fun delete(hitTable: HitTable) {
            hitToDelete = hitTable
        }

        override fun observeHits(): LiveData<List<HitTable>> {
            return hitsMutLivDat
        }

        override suspend fun updateLocal(toInsert: List<HitTable>) {
            listHits.addAll(toInsert)
        }

    }

}