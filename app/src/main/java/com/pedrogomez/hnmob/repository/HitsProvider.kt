package com.pedrogomez.hnmob.repository

import androidx.lifecycle.LiveData
import com.pedrogomez.hnmob.models.api.HitResponse
import com.pedrogomez.hnmob.models.api.toPresentationModel
import com.pedrogomez.hnmob.models.db.HitTable
import com.pedrogomez.hnmob.repository.local.LocalDataSource
import com.pedrogomez.hnmob.repository.remote.RemoteDataSource
import com.pedrogomez.hnmob.utils.extensions.print
import javax.inject.Inject

/**
 * this class is for get which repo is going to be consumed
 * */
class HitsProvider @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
    ) : Repository {

    override suspend fun loadHits(page:Int) {
        val response = remoteDataSource.getHitsData(page)
        try{
            updateLocalWithRemote(
                response?.hits?.map(HitResponse::toPresentationModel) ?: ArrayList<HitTable>()
            )
        }catch (e:Exception){
            "Ktor_request loadHits: ${e.message}".print()
        }
    }

    override suspend fun delete(hitItem: HitTable) {
        localDataSource.delete(hitItem)
    }

    override suspend fun getAllHits() : List<HitTable> {
        return localDataSource.getAllHits()
    }

    suspend fun updateLocalWithRemote(toInsert:List<HitTable>){
        localDataSource.updateLocal(toInsert)
    }

    override fun observeHits(): LiveData<List<HitTable>> {
        return localDataSource.observeHits()
    }

}

interface Repository{

    suspend fun loadHits(page:Int)
    suspend fun delete(hitItem: HitTable)
    suspend fun getAllHits(): List<HitTable>
    fun observeHits(): LiveData<List<HitTable>>

}