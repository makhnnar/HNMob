package com.pedrogomez.hnmob.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pedrogomez.hnmob.models.api.HitResponse
import com.pedrogomez.hnmob.models.api.HitsListResponse
import com.pedrogomez.hnmob.models.db.HitTable
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.junit.After
import org.junit.Assert.*

class HitsProviderTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private lateinit var SUT: HitsProvider

    val PAGE = 1

    companion object{
        val RESPONSE = HitsListResponse(
            listOf(
                HitResponse(
                    "objectId",
                    "author",
                    1000,
                    "story_title",
                    "story_url",
                    "title",
                    "url"
                )
            )
        )
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
                assertEquals(response, RESPONSE)
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

    private fun failed() {
        remoteDataSourceMock.failed = true
    }

    //getAllHits_getAllHits_returnedSuccess
    //delete_delete_passedParamsSuccess
    //observeHits_observeHits_returnedSuccess
    //updateLocalWithRemote_insert_passedParamsSuccess

    class RemoteDataSourceMock : HitsProvider.RemoteDataSource {

        var page = 0

        var failed = false

        override suspend fun getHitsData(page: Int): HitsListResponse? {
            this.page = page
            return if(failed) null else RESPONSE
        }

    }

    class LocalDataSourceMock : HitsProvider.LocalDataSource{

        override suspend fun getAllHits(): List<HitTable> {
            return emptyList()
        }

        override suspend fun insert(hitTable: HitTable) {

        }

        override suspend fun delete(hitTable: HitTable) {

        }

        override fun observeHits(): LiveData<List<HitTable>> {
            return MutableLiveData<List<HitTable>>()
        }

    }

}