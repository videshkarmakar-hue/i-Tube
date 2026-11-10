package com.videshkarmakar.itube.innertube.pages

import com.videshkarmakar.itube.innertube.models.YTItem

data class LibraryContinuationPage(
    val items: List<YTItem>,
    val continuation: String?,
)
