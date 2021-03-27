package com.pedrogomez.hnmob.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedrogomez.hnmob.models.db.HitTable
import com.pedrogomez.hnmob.models.result.Result
import kotlinx.coroutines.*

class SharedHitsViewModel(
    private val repository: Repository
) : ViewModel(){

    /*val scope : CoroutineScope = CoroutineScope(
        Dispatchers.IO
    )*/

    private val _hitsListLiveData : LiveData<List<HitTable>> = repository.observeHits()

    val hitsListLiveData : LiveData<List<HitTable>> = _hitsListLiveData

    val selectedHitLiveData = MutableLiveData<HitTable>()

    val hitsListStateApi = MutableLiveData<Result>()

    fun loadContent(){
        viewModelScope.launch {
            repository.loadHits(0)
        }
    }

    fun loadMore(page:Int){
        viewModelScope.launch {
            repository.loadHits(page)
        }
    }

    fun saveSelected(productItem: HitTable){
        selectedHitLiveData.value = productItem
    }

    override fun onCleared() {
        super.onCleared()
        //scope.coroutineContext.cancelChildren()
    }

    fun delete(hitItem: HitTable) {
        viewModelScope.launch {
            repository.delete(hitItem)
        }
    }

    interface Repository{

        suspend fun loadHits(page:Int)
        suspend fun delete(hitItem: HitTable)
        fun observeHits(): LiveData<List<HitTable>>

    }

}