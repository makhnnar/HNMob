package com.pedrogomez.hnmob.models.api

import kotlinx.serialization.Serializable

@Serializable
data class AuthorResponse(
    val matchLevel: String,
    val matchedWords: List<String>,
    val value: String
)