package com.videshkarmakar.itube.innertube.models.response

import com.videshkarmakar.itube.innertube.models.NavigationEndpoint
import kotlinx.serialization.Serializable

@Serializable
data class ResolveUrlResponse(
    val endpoint: NavigationEndpoint? = null,
)
