package com.pedrogomez.hnmob.view.hitsslist.view.listadapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedrogomez.hnmob.R
import com.pedrogomez.hnmob.databinding.ViewHolderHitBinding
import com.pedrogomez.hnmob.models.db.HitTable
import com.pedrogomez.hnmob.view.hitsslist.view.swipecontroler.SwipeController

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

    var data: HitTable? = null

    private var buttonsActions: SwipeController.SwipeControllerActions? = null

    init {
        binding = ViewHolderHitBinding.bind(itemView)
        context = parent.context
    }

    fun setData(
        data: HitTable,
        onClickItemListener: OnClickItemListener,
        buttonsActions: SwipeController.SwipeControllerActions
    ) {
        Log.i("delete","${data.isDeleted} :O")
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
        this.data = data
        this.buttonsActions = buttonsActions
    }

    fun excecutesDeleteOption(){
        data?.let {
            Log.i("Deleting","${it.story_title} :O")
            buttonsActions?.deleted(it)
        }
    }

    interface OnClickItemListener{
        fun goToItemDetail(data: HitTable)
    }

    interface SwipeActions {

        //fun onLeftClicked(position: Int)

        fun deleted(data: HitTable)

    }

}