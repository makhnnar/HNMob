package com.pedrogomez.hnmob.repository.local

import androidx.lifecycle.LiveData
import com.pedrogomez.hnmob.models.db.HitTable
import com.pedrogomez.hnmob.repository.HitsProvider
import com.pedrogomez.hnmob.utils.extensions.print
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HitsLocalRepo(
    private val hitsDao: HitsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : HitsProvider.LocalDataSource {

    override suspend fun getAllHits() : List<HitTable> = withContext(ioDispatcher) {
        hitsDao.getAllHits()
    }

    override suspend fun insert(hitTable: HitTable) = withContext(ioDispatcher) {
        hitsDao.insert(
                hitTable
        )
    }

    override suspend fun delete(hitTable: HitTable) = withContext(ioDispatcher) {
        hitTable.isDeleted = true
        val result = hitsDao.delete(
                hitTable
        )
        "delete: ${hitTable.objectID} => $result".print()
    }

    override fun observeHits(): LiveData<List<HitTable>> {
        return hitsDao.observeHits()
    }

    override suspend fun updateLocal(toInsert: List<HitTable>) {
        toInsert.forEach {
            insert(it)
        }
    }

}