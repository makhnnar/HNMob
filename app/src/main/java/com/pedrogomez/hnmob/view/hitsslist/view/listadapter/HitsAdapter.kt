package com.pedrogomez.hnmob.view.hitsslist.view.listadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pedrogomez.hnmob.models.db.HitTable
import com.pedrogomez.hnmob.utils.extensions.print
import com.pedrogomez.hnmob.view.hitsslist.view.swipecontroler.SwipeController

class HitsAdapter(
    private val onClickItemListener: HitViewHolder.OnClickItemListener,
    private val buttonsActions: SwipeController.SwipeControllerActions
) : ListAdapter<HitTable,HitViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HitViewHolder {
        val inflater = LayoutInflater.from(
            parent.context
        )
        return HitViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(
        holder: HitViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        holder.setData(
            item,
            onClickItemListener,
            buttonsActions
        )
    }
}