package com.pedrogomez.hnmob.viewmodel

import androidx.lifecycle.*
import com.pedrogomez.hnmob.models.db.HitTable
import com.pedrogomez.hnmob.models.result.Result
import com.pedrogomez.hnmob.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SharedHitsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel(){

    private val _hitsListLiveData : LiveData<List<HitTable>> = repository.observeHits()

    val hitsListLiveData = _hitsListLiveData

    val selectedHitLiveData = MutableLiveData<HitTable>()

    val loaderData = MutableLiveData<Result<Boolean>>()

    fun reloadContent(){
        setLoadingState()
        viewModelScope.launch {
            repository.loadHits(0)
            setSuccessState()
        }
    }

    fun setLoadingState(){
        loaderData.postValue(
            Result.Loading
        )
    }

    fun setSuccessState(){
        loaderData.postValue(
            Result.Success(true)
        )
    }

    fun loadFirstPage(){
        loadMore(0)
    }

    fun loadMore(page:Int){
        setLoadingState()
        viewModelScope.launch {
            repository.loadHits(page)
            setSuccessState()
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

}