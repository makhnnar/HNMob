package com.pedrogomez.hnmob.repository

import androidx.lifecycle.LiveData
import com.pedrogomez.hnmob.models.api.HitResponse
import com.pedrogomez.hnmob.models.api.HitsListResponse
import com.pedrogomez.hnmob.models.api.toPresentationModel
import com.pedrogomez.hnmob.models.db.HitTable
import com.pedrogomez.hnmob.repository.remote.HitsApiRepository
import com.pedrogomez.hnmob.utils.extensions.print
import com.pedrogomez.hnmob.view.viewmodel.SharedHitsViewModel

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

    suspend fun updateLocalWithRemote(toInsert:List<HitTable>){
        toInsert.forEach {
            localDataSource.insert(it)
        }
    }

    override fun observeHits(): LiveData<List<HitTable>> {
        return localDataSource.observeHits()
    }

    interface LocalDataSource{
        suspend fun getAllHits(): List<HitTable>
        suspend fun insert(hitTable: HitTable)
        fun observeHits(): LiveData<List<HitTable>>
    }

    interface RemoteDataSource{
        suspend fun getHitsData(page:Int): HitsListResponse?
    }

}