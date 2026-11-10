package com.videshkarmakar.itube.ui.screens.player

import android.content.Context
import com.videshkarmakar.itube.data.comments.CommentsPageResult
import com.videshkarmakar.itube.data.engagement.VideoEngagementSignals
import com.videshkarmakar.itube.data.engagement.VideoEngagementUseCase
import com.videshkarmakar.itube.data.local.ChannelSubscription
import com.videshkarmakar.itube.data.local.HomeFeedCacheRepository
import com.videshkarmakar.itube.data.local.LikedVideosRepository
import com.videshkarmakar.itube.data.local.PlayerPreferences
import com.videshkarmakar.itube.data.local.PlaylistRepository
import com.videshkarmakar.itube.data.local.SubscriptionRepository
import com.videshkarmakar.itube.data.local.VideoQuality
import com.videshkarmakar.itube.data.local.ViewHistory
import com.videshkarmakar.itube.data.local.entity.WatchHistoryEntity
import com.videshkarmakar.itube.data.localmedia.LocalSubtitles
import com.videshkarmakar.itube.data.model.Comment
import com.videshkarmakar.itube.data.model.Video
import com.videshkarmakar.itube.data.music.model.MusicTrack
import com.videshkarmakar.itube.data.recommendation.FlowNeuroEngine
import com.videshkarmakar.itube.data.repository.LiveChatRepository
import com.videshkarmakar.itube.data.repository.SponsorBlockRepository
import com.videshkarmakar.itube.data.repository.YouTubeRepository
import com.videshkarmakar.itube.data.transcript.TranscriptRepository
import com.videshkarmakar.itube.data.video.AutoDownloadTrigger
import com.videshkarmakar.itube.data.video.OfflineSubtitleStore
import com.videshkarmakar.itube.data.video.VideoDownloadManager
import com.videshkarmakar.itube.innertube.YouTube
import com.videshkarmakar.itube.player.EnhancedMusicPlayerManager
import com.videshkarmakar.itube.player.EnhancedPlayerManager
import com.videshkarmakar.itube.player.GlobalPlayerState
import com.videshkarmakar.itube.player.LifecyclePlaybackSettings
import com.videshkarmakar.itube.player.error.PlayerDiagnostics
import com.videshkarmakar.itube.player.state.EnhancedPlayerState
import com.videshkarmakar.itube.player.stream.CaptionTrackResolver
import com.videshkarmakar.itube.player.stream.InnerTubeVideoStreamExtractor
import com.videshkarmakar.itube.player.stream.PlaybackLoadResolver
import com.videshkarmakar.itube.player.stream.UpcomingPremiereProbe
import com.videshkarmakar.itube.utils.NetworkState
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import org.schabi.newpipe.extractor.Page

/**
 * Characterisation harness for [VideoPlayerViewModel]: every constructor dependency is a relaxed
 * mock, every static object the ViewModel reaches is a mockk object spy, and every flow the init
 * block collects is backed by a real [MutableStateFlow]/[MutableSharedFlow] so tests can drive it.
 *
 * The defaults describe a healthy, online, empty device: no downloads, no restored session, no
 * music playing, RYD disabled, Auto quality. Individual tests override what they need.
 */
internal class VideoPlayerViewModelHarness(
    private val testDispatcher: TestDispatcher,
) {
    val context: Context = mockk(relaxed = true)
    val repository: YouTubeRepository = mockk(relaxed = true)
    val transcriptRepository: TranscriptRepository = mockk(relaxed = true)
    val viewHistory: ViewHistory = mockk(relaxed = true)
    val subscriptionRepository: SubscriptionRepository = mockk(relaxed = true)
    val likedVideosRepository: LikedVideosRepository = mockk(relaxed = true)
    val playlistRepository: PlaylistRepository = mockk(relaxed = true)
    val playerPreferences: PlayerPreferences = mockk(relaxed = true)
    val autoDownload: AutoDownloadTrigger = mockk(relaxed = true)
    val videoDownloadManager: VideoDownloadManager = mockk(relaxed = true)
    val offlineSubtitleStore: OfflineSubtitleStore = mockk(relaxed = true)
    val localSubtitles: LocalSubtitles = mockk(relaxed = true)
    val sponsorBlockRepository: SponsorBlockRepository = mockk(relaxed = true)
    val liveChatRepository: LiveChatRepository = mockk(relaxed = true)
    val homeFeedCacheRepository: HomeFeedCacheRepository = mockk(relaxed = true)
    val playerManager: EnhancedPlayerManager = mockk(relaxed = true)
    var startVideosPaused = false
    val videoStats: com.videshkarmakar.itube.data.stats.VideoStatsRecorder = mockk(relaxed = true)

    /**
     * The real use case over the mocked repositories: every engagement assertion in the suite is
     * written against [subscriptionRepository]/[likedVideosRepository], so the seam under test
     * stays the repository call, not the use case.
     */
    val engagement: VideoEngagementUseCase by lazy {
        VideoEngagementUseCase(
            subscriptionRepository = subscriptionRepository,
            likedVideosRepository = likedVideosRepository,
            signals = VideoEngagementSignals(context, repository),
            videoStats = videoStats,
        )
    }

    val playerState = MutableStateFlow(EnhancedPlayerState())
    val playbackAbandonedEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val queueVideos = MutableStateFlow<List<Video>>(emptyList())
    val videoQueueStore = mockk<com.videshkarmakar.itube.data.video.VideoQueueStore>(relaxed = true)
    val musicCurrentTrack = MutableStateFlow<MusicTrack?>(null)
    val autoplayEnabled = MutableStateFlow(true)
    val continueWatchingEnabled = MutableStateFlow(true)
    val rytdEnabled = MutableStateFlow(false)
    val isSubscribed = MutableStateFlow(false)
    val subscription = MutableStateFlow<ChannelSubscription?>(null)
    val likeState = MutableStateFlow<String?>(null)

    init {
        mockkObject(EnhancedPlayerManager.Companion)
        every { EnhancedPlayerManager.getInstance() } returns playerManager
        every { playerManager.playerState } returns playerState
        every { playerManager.playbackAbandonedEvent } returns playbackAbandonedEvent
        every { playerManager.queueVideos } returns queueVideos
        every { playerManager.currentQueueIndexState } returns MutableStateFlow(-1)
        every { playerManager.getPlayer() } returns null
        every { playerManager.isPreparedForPlayback(any()) } returns false
        every { playerManager.isReachedByQueueAdvance(any()) } returns false

        // Reset the real singleton before spying it so the reset is not a recorded call.
        GlobalPlayerState.setCurrentVideo(null)
        GlobalPlayerState.setExplicitBackgroundPlaybackActive(false)
        GlobalPlayerState.hideMiniPlayer()
        mockkObject(GlobalPlayerState)

        mockkObject(EnhancedMusicPlayerManager)
        every { EnhancedMusicPlayerManager.currentTrack } returns musicCurrentTrack
        every { EnhancedMusicPlayerManager.stop() } just Runs
        every { EnhancedMusicPlayerManager.clearCurrentTrack() } just Runs

        mockkObject(NetworkState)
        every { NetworkState.isOnWifi(any()) } returns false
        every { NetworkState.isOnline(any()) } returns true

        mockkObject(InnerTubeVideoStreamExtractor)
        coEvery { InnerTubeVideoStreamExtractor.extract(any(), any()) } returns null

        mockkObject(YouTube)
        coEvery {
            YouTube.player(any(), any(), any(), any(), any(), any(), any())
        } returns Result.failure(IllegalStateException("premiere probe stubbed"))

        mockkObject(PlayerDiagnostics)
        every { PlayerDiagnostics.logWarning(any(), any()) } just Runs

        mockkObject(FlowNeuroEngine.Companion)
        coEvery { FlowNeuroEngine.onVideoInteraction(any<Context>(), any(), any(), any()) } just Runs
        every { FlowNeuroEngine.onVideoInteractionAsync(any(), any(), any(), any()) } just Runs

        every { context.applicationContext } returns context
        every { context.getString(any()) } answers { "res:${firstArg<Int>()}" }
        every { context.getString(any(), *anyVararg()) } answers { "res:${firstArg<Int>()}" }

        every { playerPreferences.shortsContentEnabled } returns flowOf(true)
        every { playerPreferences.originalTitles } returns flowOf(false)
        every { playerPreferences.effectiveVideoNotesEnabled } returns flowOf(false)
        every { playerPreferences.miniPlayerContinueWatchingEnabled } returns continueWatchingEnabled
        every { playerPreferences.autoplayEnabled } returns autoplayEnabled
        every { playerPreferences.upcomingVideoReminderIds } returns flowOf(emptySet())
        every { playerPreferences.rytdEnabled } returns rytdEnabled
        every { playerPreferences.defaultQualityWifi } returns flowOf(VideoQuality.AUTO)
        every { playerPreferences.defaultQualityCellular } returns flowOf(VideoQuality.AUTO)
        every { playerPreferences.preferredAudioLanguage } returns flowOf("original")
        every { playerPreferences.preferredSubtitleLanguage } returns flowOf(CaptionTrackResolver.NO_PREFERRED_LANGUAGE)
        every { playerPreferences.videoCodecPriority } returns flowOf("auto")
        every { playerPreferences.rememberPlaybackSpeed } returns flowOf(false)
        every { playerPreferences.playbackSpeed } returns flowOf(1f)
        every { playerPreferences.musicAtNormalSpeed } returns flowOf(false)
        every { playerPreferences.speedPerChannel } returns flowOf(false)

        coEvery { viewHistory.getLatestUnfinishedVideo() } returns null
        every { viewHistory.getPlaybackPosition(any()) } returns flowOf(0L)
        coEvery { viewHistory.getSavedPosition(any()) } returns 0L

        coEvery { videoDownloadManager.findLocalCopy(any()) } returns null
        coEvery { videoDownloadManager.getSponsorBlockData(any()) } returns null
        coEvery { offlineSubtitleStore.load(any()) } returns emptyList()
        coEvery { localSubtitles.picked(any()) } returns emptyList()
        coEvery { localSubtitles.beside(any()) } returns emptyList()

        coEvery { repository.getComments(any()) } returns (emptyList<Comment>() to null as Page?)
        coEvery { repository.getVideoComments(any(), any()) } returns CommentsPageResult.EMPTY

        every { subscriptionRepository.isSubscribed(any()) } returns isSubscribed
        every { subscriptionRepository.getSubscription(any()) } returns subscription
        every { likedVideosRepository.getLikeState(any()) } returns likeState
    }

    fun createViewModel(): VideoPlayerViewModel =
        VideoPlayerViewModel(
            context = context,
            repository = repository,
            transcriptRepository = transcriptRepository,
            viewHistory = viewHistory,
            engagement = engagement,
            playlistRepository = playlistRepository,
            playerPreferences = playerPreferences,
            videoDownloadManager = videoDownloadManager,
            videoQueueStore = videoQueueStore,
            watchLaterCleanup = mockk(relaxed = true),
            offlineSubtitleStore = offlineSubtitleStore,
            localSubtitles = localSubtitles,
            sponsorBlockRepository = sponsorBlockRepository,
            liveChatRepository = liveChatRepository,
            homeFeedCacheRepository = homeFeedCacheRepository,
            playerManager = playerManager,
            upcomingPremiereProbe = UpcomingPremiereProbe(),
            playbackResolver =
                PlaybackLoadResolver(
                    repository = repository,
                    viewHistory = viewHistory,
                    playerPreferences = playerPreferences,
                    videoDownloadManager = videoDownloadManager,
                    sponsorBlockRepository = sponsorBlockRepository,
                    networkDispatcher = testDispatcher,
                    ioDispatcher = testDispatcher,
                ),
            notesRepository = mockk(relaxed = true),
            videoStats = videoStats,
            localMediaDetails = mockk { coEvery { enrich(any()) } returns null },
            lifecyclePlayback = mockk { every { settings } answers { LifecyclePlaybackSettings(startVideosPaused = startVideosPaused) } },
            autoDownload = autoDownload,
            networkDispatcher = testDispatcher,
            ioDispatcher = testDispatcher,
        )

    fun close() {
        unmockkAll()
    }

    companion object {
        fun video(
            id: String,
            title: String = "Title $id",
            channelId: String = "channel_$id",
            isUpcoming: Boolean = false,
        ): Video =
            Video(
                id = id,
                title = title,
                channelName = "Channel $id",
                channelId = channelId,
                thumbnailUrl = "https://example.invalid/$id.jpg",
                duration = 120,
                viewCount = 1L,
                uploadDate = "2026-01-01",
                channelThumbnailUrl = "https://example.invalid/$channelId-avatar.jpg",
                isUpcoming = isUpcoming,
            )

        fun historyEntity(
            id: String,
            positionMs: Long = 30_000L,
            durationMs: Long = 120_000L,
        ): WatchHistoryEntity =
            WatchHistoryEntity(
                videoId = id,
                position = positionMs,
                duration = durationMs,
                timestamp = 1_000L,
                title = "Title $id",
                thumbnailUrl = "https://example.invalid/$id.jpg",
                channelName = "Channel $id",
                channelId = "channel_$id",
                isMusic = false,
            )
    }
}
