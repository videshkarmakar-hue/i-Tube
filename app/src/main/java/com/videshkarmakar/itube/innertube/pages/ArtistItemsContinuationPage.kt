package com.videshkarmakar.itube.innertube.pages

import com.videshkarmakar.itube.innertube.models.YTItem

data class ArtistItemsContinuationPage(
    val items: List<YTItem>,
    val continuation: String?,
)
