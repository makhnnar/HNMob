package com.pedrogomez.hnmob

import android.app.Application
import com.pedrogomez.hnmob.di.dbModule
import com.pedrogomez.hnmob.di.dbProvider
import com.pedrogomez.hnmob.di.networkModule
import com.pedrogomez.hnmob.view.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class HNMobAplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(
                this@HNMobAplication
            )
            androidLogger()
            modules(
                listOf(
                    networkModule,
                    productsRepository,
                    viewModelListModule,
                    dbModule,
                    dbProvider,
                    firebaseAuth,
                    firebaseDataBase,
                    authViewModel,
                    commentsViewModel
                )
            )
        }
    }

}