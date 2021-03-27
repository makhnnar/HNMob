package com.pedrogomez.hnmob.models.api

import kotlinx.serialization.Serializable

@Serializable
data class HighlightResultResponse(
        val author: AuthorResponse,
        val comment_text: CommentTextResponse,
        val story_title: StoryTitleResponse,
        val story_url: StoryUrlResponse,
        val title: TitleResponse,
        val url: UrlResponse
)