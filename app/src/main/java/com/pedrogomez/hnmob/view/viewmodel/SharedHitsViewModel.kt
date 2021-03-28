package com.pedrogomez.hnmob.view.viewmodel

import androidx.lifecycle.*
import com.pedrogomez.hnmob.models.db.HitTable
import com.pedrogomez.hnmob.models.result.Result
import kotlinx.coroutines.*

class SharedHitsViewModel(
    private val repository: Repository
) : ViewModel(){

    private val _hitsListLiveData : LiveData<List<HitTable>> = repository.observeHits()

    val hitsListLiveData : LiveData<List<HitTable>> = _hitsListLiveData

    val selectedHitLiveData = MutableLiveData<HitTable>()

    val loaderData = MutableLiveData<Result<Boolean>>()

    fun reloadContent(){
        loaderData.postValue(
            Result.Loading
        )
        viewModelScope.launch {
            repository.loadHits(0)
            loaderData.postValue(
                Result.Success(true)
            )
        }
    }

    fun loadFirstPage(){
        loadMore(0)
    }

    fun loadMore(page:Int){
        loaderData.postValue(
            Result.Loading
        )
        viewModelScope.launch {
            repository.loadHits(page)
            loaderData.postValue(
                Result.Success(true)
            )
        }
    }

    fun saveSelected(productItem: HitTable){
        selectedHitLiveData.value = productItem
    }

    fun delete(hitItem: HitTable) {
        viewModelScope.launch {
            repository.delete(hitItem)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    interface Repository{

        suspend fun loadHits(page:Int)
        suspend fun delete(hitItem: HitTable)
        suspend fun getAllHits(): Result<List<HitTable>>
        fun observeHits(): LiveData<List<HitTable>>

    }

}