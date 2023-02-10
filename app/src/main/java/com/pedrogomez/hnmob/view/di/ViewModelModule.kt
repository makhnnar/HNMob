package com.pedrogomez.hnmob.view.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pedrogomez.hnmob.repository.HitsProvider
import com.pedrogomez.hnmob.repository.Repository
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
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    fun providesFirebaseDatabase() : FirebaseDatabase {
        Firebase.database.useEmulator("10.0.2.2", 9000)
        return Firebase.database
    }

    @Provides
    fun providesFirebaseAuth(@ApplicationContext appContext: Context) : FirebaseAuth {
        Firebase.auth.useEmulator("10.0.2.2", 9099)
        return Firebase.auth
    }

}

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindHitsProvider(
        hitsProvider: HitsProvider
    ): Repository

}

