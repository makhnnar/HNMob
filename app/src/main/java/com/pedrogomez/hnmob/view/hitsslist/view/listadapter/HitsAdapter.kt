package com.pedrogomez.hnmob.view.hitsslist.view.listadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedrogomez.hnmob.models.db.HitTable
import com.pedrogomez.hnmob.utils.extensions.print

class HitsAdapter(
    private val onClickItemListener: HitViewHolder.OnClickItemListener
) : RecyclerView.Adapter<HitViewHolder>() {

    private var items: ArrayList<HitTable> = ArrayList()

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
        holder.setData(
            items[position],
            onClickItemListener
        )
    }

    override fun getItemCount() = items.size

    fun setData(hitItems: List<HitTable>?) {
        hitItems?.let {
            items.clear()
            items.addAll(it)
            "size in adapter ${items.size}".print()
            notifyItemInserted(items.size)
        }
    }

    fun clearData(){
        items.clear()
        notifyDataSetChanged()
    }
}