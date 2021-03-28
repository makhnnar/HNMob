package com.pedrogomez.hnmob.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pedrogomez.hnmob.models.db.HitTable
import com.pedrogomez.hnmob.models.result.Result
import com.pedrogomez.hnmob.util.DataHelper
import com.pedrogomez.hnmob.util.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class SharedHitsViewModelTest {

    private lateinit var SUT: SharedHitsViewModel

    var reposotoryTD = RepositoryTD()

    val PAGE = 1

    @Before
    fun setUp() {
        SUT = SharedHitsViewModel(
            reposotoryTD
        )
    }

    @Test
    fun setLoadingState_loaderDataUpdate() {
        runBlocking {
            SUT.setLoadingState()
            assertEquals(
                SUT.loaderData.getOrAwaitValue(),
                Result.Loading
            )
        }
    }

    @Test
    fun setSuccessState_loaderDataUpdate() {
        runBlocking {
            SUT.setSuccessState()
            assertEquals(
                SUT.loaderData.getOrAwaitValue(),
                Result.Success(true)
            )
        }
    }

    @Test
    fun saveSelected_selectedHitLiveDataUpdated() {
        runBlocking {
            SUT.saveSelected(DataHelper.HITTABLE)
            assertEquals(
                SUT.selectedHitLiveData.getOrAwaitValue(),
                DataHelper.HITTABLE
            )
        }
    }

    @Test
    fun delete_delete_successPassedParam() {
        runBlocking {
            SUT.delete(DataHelper.HITTABLE)
            assertEquals(
                reposotoryTD.toDelete,
                DataHelper.HITTABLE
            )
        }
    }

    @Test
    fun loadMore_loadHits_successPassedParam() {
        runBlocking {
            SUT.loadMore(PAGE)
            assertEquals(
                reposotoryTD.page,
                PAGE
            )
        }
    }

    class RepositoryTD : SharedHitsViewModel.Repository{

        var toDelete : HitTable? = null

        var page = 0

        override suspend fun loadHits(page: Int) {
            this.page = page
        }

        override suspend fun delete(hitItem: HitTable) {
            toDelete = hitItem
        }

        override suspend fun getAllHits(): List<HitTable> {
            //never used
            return emptyList()
        }

        override fun observeHits(): LiveData<List<HitTable>> {
            //used on viewmodel class creation. it's binding to DB
            return DataHelper.LIVEHISTDATA
        }

    }

}