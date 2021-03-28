package com.pedrogomez.hnmob.repository

import androidx.lifecycle.LiveData
import com.pedrogomez.hnmob.models.api.HitResponse
import com.pedrogomez.hnmob.models.api.HitsListResponse
import com.pedrogomez.hnmob.models.api.toPresentationModel
import com.pedrogomez.hnmob.models.db.HitTable
import com.pedrogomez.hnmob.utils.extensions.print
import com.pedrogomez.hnmob.view.viewmodel.SharedHitsViewModel

import com.pedrogomez.hnmob.models.result.Result

/**
 * this class is for get which repo is going to be consumed
 * */
class HitsProvider(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
    ) : SharedHitsViewModel.Repository {

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

    override suspend fun getAllHits() : Result<List<HitTable>> {
        return localDataSource.getAllHits()
    }

    suspend fun updateLocalWithRemote(toInsert:List<HitTable>){
        toInsert.forEach {
            localDataSource.insert(it)
        }
    }

    override fun observeHits(): LiveData<List<HitTable>> {
        return localDataSource.observeHits()
    }

    interface LocalDataSource{
        suspend fun getAllHits(): Result<List<HitTable>>
        suspend fun insert(hitTable: HitTable)
        suspend fun delete(hitTable: HitTable)
        fun observeHits(): LiveData<List<HitTable>>
    }

    interface RemoteDataSource{
        suspend fun getHitsData(page:Int): HitsListResponse?
    }

}