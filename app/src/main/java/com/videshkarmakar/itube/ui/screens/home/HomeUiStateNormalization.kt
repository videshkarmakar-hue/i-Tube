package com.videshkarmakar.itube.ui.screens.home

import com.videshkarmakar.itube.data.local.VideoHistoryEntry
import com.videshkarmakar.itube.data.model.Video
import com.videshkarmakar.itube.data.model.distinctByNonBlankKeyOrSelf

internal fun HomeUiState.withUniqueLazyContent(): HomeUiState {
    val uniqueVideos = videos.distinctByNonBlankKeyOrSelf(Video::id)
    val uniqueShorts = shorts.distinctByNonBlankKeyOrSelf(Video::id)
    val uniqueHistory = continueWatchingVideos.distinctByNonBlankKeyOrSelf(VideoHistoryEntry::videoId)
    return if (
        uniqueVideos === videos &&
        uniqueShorts === shorts &&
        uniqueHistory === continueWatchingVideos
    ) {
        this
    } else {
        copy(
            videos = uniqueVideos,
            shorts = uniqueShorts,
            continueWatchingVideos = uniqueHistory,
        )
    }
}
