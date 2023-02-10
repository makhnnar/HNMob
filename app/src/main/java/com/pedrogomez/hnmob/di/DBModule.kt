package com.pedrogomez.hnmob.di

import android.content.Context
import androidx.room.Room
import com.pedrogomez.hnmob.repository.local.HitsDao
import com.pedrogomez.hnmob.repository.local.HitsDataBase
import com.pedrogomez.hnmob.repository.local.HitsLocalRepo
import com.pedrogomez.hnmob.repository.local.LocalDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): HitsDataBase {
        return Room.databaseBuilder(
            appContext,
            HitsDataBase::class.java,
            "Hits.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideHitsDao(database: HitsDataBase): HitsDao {
        return database.hits()
    }

    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

}

@Module
@InstallIn(ViewModelComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    abstract fun bindHitsLocalRepo(
        hitsLocalRepo: HitsLocalRepo
    ): LocalDataSource

}