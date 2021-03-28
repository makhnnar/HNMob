package com.pedrogomez.hnmob.view.hitsslist.view.listadapter

import androidx.recyclerview.widget.DiffUtil
import com.pedrogomez.hnmob.models.db.HitTable

class TaskDiffCallback : DiffUtil.ItemCallback<HitTable>() {
    override fun areItemsTheSame(oldItem: HitTable, newItem: HitTable): Boolean {
        return oldItem.objectID == newItem.objectID
    }

    override fun areContentsTheSame(oldItem: HitTable, newItem: HitTable): Boolean {
        return oldItem == newItem
    }
}