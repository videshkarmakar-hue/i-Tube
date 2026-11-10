package com.videshkarmakar.itube.innertube.pages

import com.videshkarmakar.itube.innertube.models.Album
import com.videshkarmakar.itube.innertube.models.Artist
import com.videshkarmakar.itube.innertube.models.MusicResponsiveListItemRenderer
import com.videshkarmakar.itube.innertube.models.MusicShelfRenderer
import com.videshkarmakar.itube.innertube.models.SongItem
import com.videshkarmakar.itube.innertube.models.getItems
import com.videshkarmakar.itube.innertube.models.oddElements
import com.videshkarmakar.itube.innertube.utils.parseTime

data class HistoryPage(
    val sections: List<HistorySection>?,
) {
    data class HistorySection(
        val title: String,
        val songs: List<SongItem>,
    )

    companion object {
        fun fromMusicShelfRenderer(renderer: MusicShelfRenderer): HistorySection =
            HistorySection(
                title =
                    renderer.title
                        ?.runs
                        ?.firstOrNull()
                        ?.text!!,
                songs =
                    renderer.contents?.getItems()?.mapNotNull {
                        fromMusicResponsiveListItemRenderer(it)
                    }!!,
            )

        private fun fromMusicResponsiveListItemRenderer(renderer: MusicResponsiveListItemRenderer): SongItem? {
            return SongItem(
                id = renderer.playlistItemData?.videoId ?: return null,
                title =
                    renderer.flexColumns
                        .firstOrNull()
                        ?.musicResponsiveListItemFlexColumnRenderer
                        ?.text
                        ?.runs
                        ?.firstOrNull()
                        ?.text ?: return null,
                artists =
                    renderer.flexColumns.getOrNull(1)?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.oddElements()?.map {
                        Artist(
                            name = it.text,
                            id = it.navigationEndpoint?.browseEndpoint?.browseId,
                        )
                    } ?: emptyList(),
                album =
                    renderer.flexColumns.getOrNull(3)?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.firstOrNull()?.let {
                        Album(
                            name = it.text,
                            id = it.navigationEndpoint?.browseEndpoint?.browseId ?: return@let null,
                        )
                    },
                duration =
                    renderer.fixedColumns
                        ?.firstOrNull()
                        ?.musicResponsiveListItemFlexColumnRenderer
                        ?.text
                        ?.runs
                        ?.firstOrNull()
                        ?.text
                        ?.parseTime(),
                musicVideoType = renderer.musicVideoType,
                thumbnail = renderer.thumbnail?.musicThumbnailRenderer?.getThumbnailUrl() ?: return null,
                explicit =
                    renderer.badges?.find {
                        it.musicInlineBadgeRenderer?.icon?.iconType == "MUSIC_EXPLICIT_BADGE"
                    } != null,
                endpoint =
                    renderer.overlay
                        ?.musicItemThumbnailOverlayRenderer
                        ?.content
                        ?.musicPlayButtonRenderer
                        ?.playNavigationEndpoint
                        ?.watchEndpoint,
            )
        }
    }
}
