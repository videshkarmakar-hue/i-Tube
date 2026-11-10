package com.videshkarmakar.itube.ui.components.library

import com.videshkarmakar.itube.data.local.LikedVideoInfo
import com.videshkarmakar.itube.data.local.VideoHistoryEntry
import com.videshkarmakar.itube.data.model.Video
import com.videshkarmakar.itube.data.model.toMusicTrack
import com.videshkarmakar.itube.data.model.toVideo
import com.videshkarmakar.itube.data.music.DownloadedTrack
import com.videshkarmakar.itube.data.music.model.MusicTrack
import com.videshkarmakar.itube.data.video.DownloadedVideo

internal const val LIBRARY_SHELF_ITEM_LIMIT = 20

internal sealed interface LibraryMediaItem {
    val key: String

    data class VideoItem(
        val video: Video,
    ) : LibraryMediaItem {
        override val key: String = "video:${video.id}"
    }

    data class MusicItem(
        val track: MusicTrack,
    ) : LibraryMediaItem {
        override val key: String = "music:${track.videoId}"
    }

    data class DownloadedVideoItem(
        val download: DownloadedVideo,
    ) : LibraryMediaItem {
        override val key: String = "downloaded-video:${download.video.id}"
    }

    data class DownloadedMusicItem(
        val download: DownloadedTrack,
    ) : LibraryMediaItem {
        override val key: String = "downloaded-music:${download.track.videoId}"
    }
}

internal fun VideoHistoryEntry.toLibraryMediaItem(): LibraryMediaItem =
    if (isMusic) {
        LibraryMediaItem.MusicItem(toMusicTrack())
    } else {
        LibraryMediaItem.VideoItem(toVideo())
    }

internal fun LikedVideoInfo.toLibraryMediaItem(): LibraryMediaItem =
    if (isMusic) {
        LibraryMediaItem.MusicItem(toMusicTrack())
    } else {
        LibraryMediaItem.VideoItem(toVideo())
    }
