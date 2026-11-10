package com.videshkarmakar.itube.ui.screens.playlists

import com.videshkarmakar.itube.data.model.PlaylistInfo
import com.videshkarmakar.itube.data.playlist.PlaylistListOrder
import com.videshkarmakar.itube.data.playlist.sortedFor
import com.videshkarmakar.itube.ui.components.library.PlaylistOwnershipFilter

/**
 * Owned playlists lead saved ones in every order except [PlaylistListOrder.CUSTOM], where the
 * viewer's own arrangement decides.
 */
internal fun orderedPlaylists(
    owned: List<PlaylistInfo>,
    saved: List<PlaylistInfo>,
    filter: PlaylistOwnershipFilter,
    order: PlaylistListOrder,
): List<PlaylistInfo> =
    if (order == PlaylistListOrder.CUSTOM) {
        filter.select(owned, saved).sortedFor(order)
    } else {
        filter.select(owned.sortedFor(order), saved.sortedFor(order))
    }
