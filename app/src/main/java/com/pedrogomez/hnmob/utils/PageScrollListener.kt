package com.pedrogomez.hnmob.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PageScrollListener(
    private val gridLayoutManager: GridLayoutManager?
) : RecyclerView.OnScrollListener() {

    private var previousTotal = 1 // The total number of items in the dataset after the last load
    private var loading = true // True if we are still waiting for the last set of data to load.
    private val visibleThreshold = 5 // The minimum amount of items to have below your current scroll position before loading more.

    private var isEnable = false

    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    var currentPage = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        visibleItemCount = recyclerView.childCount
        if(isEnable){
            if (gridLayoutManager != null) {
                totalItemCount = gridLayoutManager.itemCount
                firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition()
            } else {
                totalItemCount = 0
                firstVisibleItem = 0
            }
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false
                    previousTotal = totalItemCount
                }
            }
            if (!loading && totalItemCount - visibleItemCount
                    <= firstVisibleItem + visibleThreshold
            ) {
                // End has been reached
                // Do something
                currentPage++
                onLoadMore(currentPage)
                loading = true
            }
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val visiblePos = gridLayoutManager?.findFirstCompletelyVisibleItemPosition()?:0
        scrollIsOnTop(visiblePos<1)
    }

    fun initFields(){
        previousTotal = 1
        loading = true
        firstVisibleItem = 0
        visibleItemCount = 0
        totalItemCount = 0
        currentPage = 0
        isEnable = false
    }

    fun enablePaging(enable:Boolean){
        isEnable = enable
    }

    abstract fun onLoadMore(currentPage: Int)

    abstract fun scrollIsOnTop(isOnTop:Boolean)

}
