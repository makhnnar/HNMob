package com.pedrogomez.hnmob.di

import android.content.Context
import com.pedrogomez.hnmob.R
import com.pedrogomez.hnmob.repository.remote.HitsApiRepository
import com.pedrogomez.hnmob.repository.remote.RemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

@Module
@InstallIn(ViewModelComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindHitsProvider(
        hitsApiRepository: HitsApiRepository
    ): RemoteDataSource

}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun providesHttpClient() : HttpClient {
        return HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    @Provides
    fun providesUrl(@ApplicationContext appContext: Context) : String {
        //return "https://hn.algolia.com/api/v1/search_by_date?query=mobile"
        return appContext.getString(R.string.url_api)
    }

}
