package com.pedrogomez.hnmob.repository.local

import androidx.lifecycle.LiveData
import com.pedrogomez.hnmob.models.db.HitTable
import com.pedrogomez.hnmob.repository.HitsProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HitsLocalRepo(
    private val hitsDao: HitsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : HitsProvider.LocalDataSource {

    override suspend fun getAllHits(): List<HitTable> {
        return hitsDao.getAllHits()
    }

    override suspend fun insert(hitTable: HitTable) = withContext(ioDispatcher) {
        hitsDao.insert(
                hitTable
        )
    }

    override fun observeHits(): LiveData<List<HitTable>> {
        return hitsDao.observeHits()
    }

}