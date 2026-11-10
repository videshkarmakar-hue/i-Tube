package com.videshkarmakar.itube.ui.screens.player.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.videshkarmakar.itube.data.model.Video
import com.videshkarmakar.itube.player.EnhancedPlayerManager
import com.videshkarmakar.itube.player.dlna.DlnaCastManager
import com.videshkarmakar.itube.player.state.EnhancedPlayerState
import com.videshkarmakar.itube.ui.components.shared.MediaDownloadDialog
import com.videshkarmakar.itube.ui.components.shared.MediaDownloadDialogCompact
import com.videshkarmakar.itube.ui.components.videoplayer.DlnaDevicePickerDialog
import com.videshkarmakar.itube.ui.screens.player.VideoPlayerViewModel
import com.videshkarmakar.itube.ui.screens.player.state.PlayerScreenState
import com.videshkarmakar.itube.ui.screens.player.state.PlayerSheet
import com.videshkarmakar.itube.ui.screens.player.state.VideoPlayerPreferencesState
import com.videshkarmakar.itube.ui.screens.player.state.VideoPlayerUiState
import kotlinx.coroutines.launch

@Composable
internal fun PlayerDialogsContainer(
    screenState: PlayerScreenState,
    playerState: EnhancedPlayerState,
    uiState: VideoPlayerUiState,
    video: Video,
    viewModel: VideoPlayerViewModel,
    prefs: VideoPlayerPreferencesState,
    hostedInSidePanel: Boolean = false,
    mediaSheetExpandedHeight: Dp? = null,
    mediaSheetCollapsedHeight: Dp = 0.dp,
    onMediaSheetProgressChange: (Float) -> Unit = {},
) {
    val playerPreferences = prefs.preferences
    val coroutineScope = rememberCoroutineScope()

    // Download Quality Dialog
    if (screenState.activeSheet == PlayerSheet.Download) {
        when (prefs.downloadDialogStyle) {
            com.videshkarmakar.itube.data.local.DownloadDialogStyle.COMPACT -> {
                MediaDownloadDialogCompact(
                    streamSizes = uiState.streamSizes,
                    innerTubeVideoFormats = uiState.innerTubeVideoFormats,
                    innerTubeAudioFormats = uiState.innerTubeAudioFormats,
                    video = video,
                    currentPlayingHeight = playerState.effectiveQuality,
                    subtitles = playerState.availableSubtitles,
                    onDismiss = { screenState.closeSheet() },
                )
            }

            com.videshkarmakar.itube.data.local.DownloadDialogStyle.FULL -> {
                MediaDownloadDialog(
                    streamSizes = uiState.streamSizes,
                    innerTubeVideoFormats = uiState.innerTubeVideoFormats,
                    innerTubeAudioFormats = uiState.innerTubeAudioFormats,
                    video = video,
                    subtitles = playerState.availableSubtitles,
                    onDismiss = { screenState.closeSheet() },
                )
            }

            null -> { }
        }
    }

    if (screenState.isSettingsOpen && !hostedInSidePanel) {
        PlayerSettingsSheetHost(
            screenState = screenState,
            playerState = playerState,
            uiState = uiState,
            viewModel = viewModel,
            playerPreferences = playerPreferences,
            scope = coroutineScope,
            rememberPlaybackSpeed = prefs.rememberPlaybackSpeed,
            ambientModeEnabled = prefs.ambientModeEnabled,
            groupedQualitySelectorEnabled = prefs.groupedQualitySelectorEnabled,
            rememberSubtitleLanguage = { language ->
                coroutineScope.launch { playerPreferences.setPreferredSubtitleLanguage(language) }
            },
            asSidePanel = false,
            expandedHeight = mediaSheetExpandedHeight,
            collapsedHeight = mediaSheetCollapsedHeight,
            pipAspectRatio = null,
            onSheetProgressChange = onMediaSheetProgressChange,
            onDismiss = { screenState.closeSheet() },
        )
    }

    if (screenState.activeSheet == PlayerSheet.Dlna) {
        val dlnaDevices by DlnaCastManager.devices.collectAsStateWithLifecycle()
        val isDlnaDiscovering by DlnaCastManager.isDiscovering.collectAsStateWithLifecycle()
        DlnaDevicePickerDialog(
            devices = dlnaDevices,
            isDiscovering = isDlnaDiscovering,
            isCasting = DlnaCastManager.isCasting,
            videoTitle = video.title,
            onDeviceSelected = { device ->
                val currentPlayerUrl =
                    EnhancedPlayerManager
                        .getInstance()
                        .getPlayer()
                        ?.currentMediaItem
                        ?.localConfiguration
                        ?.uri
                        ?.toString()
                DlnaCastManager.castStreamInfo(
                    device = device,
                    title = video.title,
                    streamInfo = null,
                    currentPlayerUrl = currentPlayerUrl,
                )
                screenState.closeSheet()
            },
            onStopCasting = {
                DlnaCastManager.disconnect()
                screenState.closeSheet()
            },
            onDismiss = {
                DlnaCastManager.stopDiscovery()
                screenState.closeSheet()
            },
        )
    }
}
