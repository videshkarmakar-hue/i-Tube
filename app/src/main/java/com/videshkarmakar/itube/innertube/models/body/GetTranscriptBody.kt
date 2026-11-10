package com.videshkarmakar.itube.innertube.models.body

import com.videshkarmakar.itube.innertube.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class GetTranscriptBody(
    val context: Context,
    val params: String,
)
