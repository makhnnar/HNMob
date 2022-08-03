package com.pedrogomez.hnmob.unittest.repository.remote

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class HitsApiRepositoryTest {

    @Before
    fun setUp() {
    }

    @Test
    fun sampleClientTest() {
        runBlocking {
            val mockEngine = MockEngine { request ->
                respond(
                    content = ByteReadChannel("""{"ip":"127.0.0.1"}"""),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            val apiClient = ApiClient(mockEngine)

            Assert.assertEquals("127.0.0.1", apiClient.getIp().ip)
        }
    }


    @After
    fun tearDown() {
    }

}

class ApiClient(engine: HttpClientEngine) {
    private val httpClient = HttpClient(engine) {
        install(JsonFeature) {
            kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
        }
    }

    suspend fun getIp(): IpResponse{
        return httpClient.request("https://api.ipify.org/?format=json") {
            method = HttpMethod.Get
        }
    }
}

@Serializable
data class IpResponse(val ip: String)