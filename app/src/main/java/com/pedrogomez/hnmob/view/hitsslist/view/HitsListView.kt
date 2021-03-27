package com.pedrogomez.hnmob.view.hitsslist.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedrogomez.hnmob.R
import com.pedrogomez.hnmob.databinding.ViewHitListBinding
import com.pedrogomez.hnmob.models.db.HitTable
import com.pedrogomez.hnmob.utils.extensions.remove
import com.pedrogomez.hnmob.utils.extensions.show
import com.pedrogomez.hnmob.view.hitsslist.view.listadapter.HitsAdapter
import com.pedrogomez.hnmob.view.hitsslist.view.listadapter.HitViewHolder

class HitsListView : ConstraintLayout,
    HitViewHolder.OnClickItemListener{

    lateinit var binding : ViewHitListBinding

    private lateinit var hitsAdapter : HitsAdapter

    var onProductListActions : OnProductListActions? = null

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
        binding.rvPokeItems.apply{
            adapter = hitsAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.srlContainer.setOnRefreshListener {
            onProductListActions?.loadAgain()
        }
        binding.btnToTop.setOnClickListener {
            binding.rvPokeItems.smoothScrollToPosition(0)
        }
    }

    fun hideBtnToTop(){
        binding.btnToTop.hide()
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

    fun setData(productItems: List<HitTable>){
        hitsAdapter.setData(productItems)
    }

    interface OnProductListActions{
        fun loadAgain()
        fun goToItemDetail(data: HitTable)
    }

    override fun goToItemDetail(data: HitTable) {
        onProductListActions?.goToItemDetail(data)
    }

}