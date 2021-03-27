package com.pedrogomez.hnmob.view.hitsslist.view.listadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedrogomez.hnmob.R
import com.pedrogomez.hnmob.databinding.ViewHolderHitBinding
import com.pedrogomez.hnmob.models.db.HitTable

class HitViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.view_holder_hit,
        parent,
        false
    )
) {
    private var context : Context

    private var binding: ViewHolderHitBinding? = null

    init {
        binding = ViewHolderHitBinding.bind(itemView)
        context = parent.context
    }

    fun setData(
        data: HitTable,
        onClickItemListener: OnClickItemListener
    ) {
        if(data.title!=null){
            binding?.tvTitle?.text = data.title
        }else{
            binding?.tvTitle?.text = data.story_title
        }
        binding?.tvAuthor?.text = data.author
        binding?.tvCreated?.text = "${data.created_at_i}"
        binding?.itemRowContainer?.setOnClickListener {
            onClickItemListener.goToItemDetail(
                data
            )
        }
    }

    interface OnClickItemListener{
        fun goToItemDetail(data: HitTable)
    }

}