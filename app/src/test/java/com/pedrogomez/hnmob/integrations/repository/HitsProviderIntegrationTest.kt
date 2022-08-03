package com.pedrogomez.hnmob.integrations.repository

import android.util.Log
import com.pedrogomez.hnmob.repository.HitsProvider
import com.pedrogomez.hnmob.repository.remote.HitsApiRepository
import com.pedrogomez.hnmob.unittest.util.JsonFileHelper
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class HitsProviderIntegrationTest {

    private lateinit var remoteDataSource : HitsProvider.RemoteDataSource
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
    }

    @Test
    fun testApiWithJsonFile(){
        runBlocking {
            val results = remoteDataSource.getHitsData(1)
            assertEquals(results?.hits?.get(0)?.story_id,32304011)
        }
    }

}