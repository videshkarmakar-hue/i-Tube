package com.videshkarmakar.itube.innertube.pages

import com.videshkarmakar.itube.innertube.models.SongItem

data class PlaylistContinuationPage(
    val songs: List<SongItem>,
    val continuation: String?,
)
