package com.pedrogomez.hnmob.view.hitsslist.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedrogomez.hnmob.R
import com.pedrogomez.hnmob.databinding.ViewHitListBinding
import com.pedrogomez.hnmob.models.db.HitTable
import com.pedrogomez.hnmob.utils.PageScrollListener
import com.pedrogomez.hnmob.utils.extensions.remove
import com.pedrogomez.hnmob.utils.extensions.show
import com.pedrogomez.hnmob.view.hitsslist.view.listadapter.HitsAdapter
import com.pedrogomez.hnmob.view.hitsslist.view.listadapter.HitViewHolder

class HitsListView : ConstraintLayout,
    HitViewHolder.OnClickItemListener,
    PageScrollListener.OnScrollEvents{

    private lateinit var pageScrollListener: PageScrollListener

    lateinit var binding : ViewHitListBinding

    private lateinit var hitsAdapter : HitsAdapter

    private lateinit var linearLayoutManager: LinearLayoutManager

    var onHitsListActions : OnHitsListActions? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        binding = ViewHitListBinding.inflate(
            LayoutInflater.from(context),
            this
        )
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.HitsListView,
            defStyle,
            0
        )
        initRecyclerView()
        a.recycle()
    }

    private fun initRecyclerView() {
        hitsAdapter = HitsAdapter(this)
        linearLayoutManager = LinearLayoutManager(context)
        pageScrollListener = PageScrollListener(
                linearLayoutManager,
                this,
                true
        )
        binding.rvPokeItems.apply{
            adapter = hitsAdapter
            layoutManager = linearLayoutManager
        }
        binding.srlContainer.setOnRefreshListener {
            onHitsListActions?.loadAgain()
        }
        binding.btnToTop.setOnClickListener {
            binding.rvPokeItems.smoothScrollToPosition(0)
        }
        binding.rvPokeItems.addOnScrollListener(
                pageScrollListener
        )
    }

    fun hideBtnToTop(){
        binding.btnToTop.hide()
    }

    fun showBtnToTop(){
        binding.btnToTop.show()
    }

    fun showLoader(){
        binding.srlContainer.isRefreshing = false
        binding.srlContainer.isEnabled = false
        binding.pbPokesLoading.show()
    }

    fun hideLoader(){
        binding.srlContainer.isRefreshing = false
        binding.srlContainer.isEnabled = true
        binding.pbPokesLoading.remove()
    }

    fun setData(hitItems: List<HitTable>){
        hitsAdapter.setData(hitItems)
    }

    interface OnHitsListActions{
        fun loadMore(page:Int)
        fun loadAgain()
        fun goToItemDetail(data: HitTable)
    }

    override fun goToItemDetail(data: HitTable) {
        onHitsListActions?.goToItemDetail(data)
    }

    override fun onLoadMore(currentPage: Int) {
        onHitsListActions?.loadMore(currentPage)
    }

    override fun scrollIsOnTop(isOnTop: Boolean) {
        if(isOnTop){
            hideBtnToTop()
            return
        }
        showBtnToTop()
    }

}