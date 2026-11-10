package com.videshkarmakar.itube.innertube.models.body

import com.videshkarmakar.itube.innertube.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class GetSearchSuggestionsBody(
    val context: Context,
    val input: String,
)
