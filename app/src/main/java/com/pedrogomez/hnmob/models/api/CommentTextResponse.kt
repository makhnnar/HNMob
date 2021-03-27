package com.pedrogomez.hnmob.models.api

import kotlinx.serialization.Serializable

@Serializable
data class CommentTextResponse(
    val fullyHighlighted: Boolean?,
    val matchLevel: String,
    val matchedWords: List<String>,
    val value: String
)