package com.pedrogomez.hnmob.models.api

import kotlinx.serialization.Serializable

@Serializable
data class HitsListResponse(
        val hits: List<HitResponse>
)