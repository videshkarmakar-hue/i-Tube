package com.videshkarmakar.itube.innertube.models.body

import com.videshkarmakar.itube.innertube.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class SearchBody(
    val context: Context,
    val query: String?,
    val params: String?,
    val continuation: String? = null,
)
