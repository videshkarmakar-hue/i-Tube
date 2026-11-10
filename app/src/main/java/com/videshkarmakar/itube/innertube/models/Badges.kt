package com.videshkarmakar.itube.innertube.models

import kotlinx.serialization.Serializable

@Serializable
data class Badges(
    val musicInlineBadgeRenderer: MusicInlineBadgeRenderer?,
) {
    @Serializable
    data class MusicInlineBadgeRenderer(
        val icon: Icon,
    )
}
