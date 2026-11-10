package com.videshkarmakar.itube.innertube.models.body

import com.videshkarmakar.itube.innertube.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class GetQueueBody(
    val context: Context,
    val videoIds: List<String>?,
    val playlistId: String?,
)
