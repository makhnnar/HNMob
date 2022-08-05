package com.pedrogomez.hnmob.integrations.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pedrogomez.hnmob.repository.HitsProvider
import com.pedrogomez.hnmob.repository.local.HitsDataBase
import com.pedrogomez.hnmob.repository.local.HitsLocalRepo
import com.pedrogomez.hnmob.repository.remote.HitsApiRepository
import com.pedrogomez.hnmob.unittest.util.DataHelper
import com.pedrogomez.hnmob.unittest.util.JsonFileHelper
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config
import java.io.IOException
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@Config(sdk = [29])
class HitsProviderIntegrationTest {

    private lateinit var remoteDataSource : HitsProvider.RemoteDataSource
    private lateinit var db: HitsDataBase
    private lateinit var localDataSource : HitsProvider.LocalDataSource
    private lateinit var hitsProvider: HitsProvider

    @Before
    fun setup(){
        val jsonTOReturn = JsonFileHelper().readFileWithoutNewLineFromResources("test_api.json")
        val engine = MockEngine { request ->
            respond(
                content = ByteReadChannel(jsonTOReturn),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val httpClient = HttpClient(engine) {
            install(JsonFeature) {
                kotlinx.serialization.json.Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            }
        }
        remoteDataSource = HitsApiRepository(
            httpClient,
            "https://hn.algolia.com/api/v1/search_by_date?query=mobile"
        )
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            HitsDataBase::class.java
        ).build()
        localDataSource = HitsLocalRepo(
            db.hits()
        )
        hitsProvider = HitsProvider(
            remoteDataSource,
            localDataSource
        )
    }

    @Test
    fun testApiWithJsonFile(){
        runBlocking {
            val results = remoteDataSource.getHitsData(1)
            assertEquals(results?.hits?.get(0)?.story_id,32304011)
        }
    }

    @Test
    fun testDataBaseImpl(){
        runBlocking {
            val hitTable = DataHelper.HITTABLE
            localDataSource.insert(hitTable)
            val hits = localDataSource.getAllHits()
            Assert.assertEquals(hits, DataHelper.HITSLIST)
        }
    }

    @Test
    fun testHitsProvider(){
        runBlocking {
            hitsProvider.loadHits(1)
            val hits = localDataSource.getAllHits()
            assert(hits.isNotEmpty())
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        //if we don't stop koin you only could get one test work at time
        stopKoin()
        db.close()
    }

}