package com.videshkarmakar.itube.ui.screens.music

import com.videshkarmakar.itube.data.model.distinctByNonBlankKey
import com.videshkarmakar.itube.innertube.models.AlbumItem
import com.videshkarmakar.itube.innertube.models.ArtistItem
import com.videshkarmakar.itube.innertube.models.PlaylistItem
import com.videshkarmakar.itube.innertube.models.SongItem
import com.videshkarmakar.itube.innertube.models.YTItem
import com.videshkarmakar.itube.innertube.pages.SearchSummaryPage

internal fun YTItem.stableIdentityKey(): String {
    if (id.isBlank()) return ""
    return when (this) {
        is SongItem -> "song:$id"
        is AlbumItem -> "album:$id"
        is PlaylistItem -> "playlist:$id"
        is ArtistItem -> "artist:$id"
    }
}

internal fun YTItem.stableLazyKey(namespace: String): String = "$namespace:${stableIdentityKey()}"

internal fun Iterable<YTItem>.distinctByStableIdentity(): List<YTItem> = distinctByNonBlankKey(YTItem::stableIdentityKey)

internal fun SearchSummaryPage.distinctItemsForLazyKeys(): SearchSummaryPage {
    val seenBySection = mutableMapOf<String, MutableSet<String>>()
    return copy(
        summaries =
            summaries.mapNotNull { summary ->
                val seenKeys = seenBySection.getOrPut("${summary.kind}:${summary.title}") { HashSet() }
                summary
                    .copy(
                        items =
                            summary.items.filter { item ->
                                val key = item.stableIdentityKey()
                                item.id.isNotBlank() && seenKeys.add(key)
                            },
                    ).takeIf { it.items.isNotEmpty() }
            },
    )
}
