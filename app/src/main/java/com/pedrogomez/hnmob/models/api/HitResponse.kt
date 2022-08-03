package com.pedrogomez.hnmob.models.api

import com.pedrogomez.hnmob.models.db.HitTable
import kotlinx.serialization.Serializable

@Serializable
data class HitResponse(
        val objectID: String,
        val author: String,
        val created_at_i: Long,
        val story_id: Long?,
        val parent_id: Long?,
        val created_at: String?,
        val story_title: String?,
        val points: String?,
        val story_text: String?,
        val comment_text: String?,
        val num_comments: String?,
        val story_url: String?,
        val title: String?,
        val url: String?,
)

fun HitResponse.toPresentationModel() : HitTable {
    return HitTable(
            objectID,
            author,
            created_at_i,
            story_title,
            story_url,
            title,
            url
    )
}