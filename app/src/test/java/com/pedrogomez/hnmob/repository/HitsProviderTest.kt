package com.pedrogomez.hnmob.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pedrogomez.hnmob.models.api.HitResponse
import com.pedrogomez.hnmob.models.api.HitsListResponse
import com.pedrogomez.hnmob.models.api.toPresentationModel
import com.pedrogomez.hnmob.models.db.HitTable
import com.pedrogomez.hnmob.util.getOrAwaitValue
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

import org.junit.After
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HitsProviderTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private lateinit var SUT: HitsProvider

    val PAGE = 1

    companion object{
        val HITTABLE = HitTable(
                "objectId",
                "author",
                1000,
                "story_title",
                "story_url",
                "title",
                "url"
        )
        val HITSRESPONSE = HitsListResponse(
            listOf(
                HitResponse(
                    "objectId",
                    "author",
                    1000,
                    "story_title",
                    "story_url",
                    "title",
                    "url"
                ),
                HitResponse(
                    "objectId1",
                    "author1",
                    10001,
                    "story_title1",
                    "story_url1",
                    "title1",
                    "url1"
                )
            )
        )
        val HITSLIST = listOf(
                HITTABLE
        )
        val LIVEHISTDATA = liveData<List<HitTable>> { HITSLIST }
    }

    var remoteDataSourceMock = RemoteDataSourceMock()
    var localDataSourceMock =  LocalDataSourceMock()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        SUT = HitsProvider(
            remoteDataSourceMock,
            localDataSourceMock
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    //onLoadHits_getHitsData_passedParamsSuccess
    @Test
    fun onLoadHits_getHitsData_passedParamsSuccess() {
        runBlocking {
            launch(Dispatchers.Main) {
                SUT.loadHits(PAGE)
                remoteDataSourceMock.getHitsData(PAGE)
                assertEquals(remoteDataSourceMock.page,PAGE)
            }
        }
    }

    //onLoadHits_getHitsData_successResponse
    @Test
    fun onLoadHits_getHitsData_successResponse() {
        runBlocking {
            launch(Dispatchers.Main) {
                SUT.loadHits(PAGE)
                val response = remoteDataSourceMock.getHitsData(PAGE)
                assertEquals(response, HITSRESPONSE)
            }
        }
    }

    //onLoadHits_getHitsData_failedResponse
    @Test
    fun onLoadHits_getHitsData_failedResponse() {
        failed()
        runBlocking {
            launch(Dispatchers.Main) {
                SUT.loadHits(PAGE)
                val response = remoteDataSourceMock.getHitsData(PAGE)
                assertNull(response)
            }
        }
    }

    //getAllHits_getAllHits_returnedSuccess
    @Test
    fun getAllHits_getAllHits_returnedSuccess() {
        runBlocking {
            launch(Dispatchers.Main) {
                val list = SUT.getAllHits()
                assertEquals(
                        list,
                        HITSLIST
                )
            }
        }
    }

    //delete_delete_passedParamsSuccess
    @Test
    fun delete_delete_passedParamsSuccess() {
        runBlocking {
            launch(Dispatchers.Main) {
                SUT.delete(HITTABLE)
                assertEquals(
                        localDataSourceMock.hitToDelete,
                        HITTABLE
                )
            }
        }
    }

    //observeHits_observeHits_returnedSuccess
    @Test
    fun observeHits_observeHits_returnedSuccess() {
        runBlocking {
            try {
                // Observe the LiveData forever
                onDataChange()
                val list = SUT.observeHits()
                // Then the new task event is triggered
                assertEquals(
                        list.getOrAwaitValue(),
                        HITSLIST
                )
            } finally {
                // Whatever happens, don't forget to remove the observer!
            }
        }
    }

    //updateLocalWithRemote_insert_passedParamsSuccess
    @Test
    fun updateLocalWithRemote_insert_passedParamsSuccess() {
        runBlocking {
            launch(Dispatchers.Main) {
                SUT.updateLocalWithRemote(
                         HITSRESPONSE.hits.map {
                             it.toPresentationModel()
                         }
                )
                assertEquals(
                        localDataSourceMock.listHits,
                        HITSRESPONSE.hits.map {
                            it.toPresentationModel()
                        }
                )
            }
        }
    }

    private fun failed() {
        remoteDataSourceMock.failed = true
    }

    private fun onDataChange() {
        localDataSourceMock.hitsMutLivDat.postValue(
                HITSLIST
        )
    }

    class RemoteDataSourceMock : HitsProvider.RemoteDataSource {

        var page = 0

        var failed = false

        override suspend fun getHitsData(page: Int): HitsListResponse? {
            this.page = page
            return if(failed) null else HITSRESPONSE
        }

    }

    class LocalDataSourceMock : HitsProvider.LocalDataSource{

        var hitToDelete : HitTable? = null

        var hitsMutLivDat = MutableLiveData<List<HitTable>>()

        var inserted : HitTable? = null

        var listHits = ArrayList<HitTable>()

        override suspend fun getAllHits(): List<HitTable> {
            return HITSLIST
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