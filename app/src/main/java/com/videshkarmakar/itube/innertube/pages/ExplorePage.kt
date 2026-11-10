package com.videshkarmakar.itube.innertube.pages

import com.videshkarmakar.itube.innertube.models.AlbumItem

data class ExplorePage(
    val newReleaseAlbums: List<AlbumItem>,
    val moodAndGenres: List<MoodAndGenres.Item>,
)
