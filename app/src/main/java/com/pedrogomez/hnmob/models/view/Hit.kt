package com.pedrogomez.hnmob.models.view

data class Hit(
        val author: String,
        val comment_text: String,
        val created_at: String,
        val created_at_i: Int,
        val num_comments: Int?,
        val objectID: String,
        val parent_id: Int,
        val points: Int?,
        val story_id: Int,
        val story_text: String?,
        val story_title: String,
        val story_url: String,
        val title: String?,
        val url: String?
)