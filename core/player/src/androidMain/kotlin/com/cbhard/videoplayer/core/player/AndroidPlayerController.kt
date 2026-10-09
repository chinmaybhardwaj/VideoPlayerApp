package com.cbhard.videoplayer.core.player

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.Format
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.TrackGroup
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import com.cbhard.videoplayer.core.model.AudioTrackItem
import com.cbhard.videoplayer.core.model.BufferProfile
import com.cbhard.videoplayer.core.model.DrmScheme
import com.cbhard.videoplayer.core.model.PlayerMetrics
import com.cbhard.videoplayer.core.model.QualityOption
import com.cbhard.videoplayer.core.model.ResizeMode
import com.cbhard.videoplayer.core.model.SubtitleTrackItem
import com.cbhard.videoplayer.core.model.Video
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
class AndroidPlayerController(
    private val context: Context
) : PlayerController {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var tickerJob: Job? = null

    private val bandwidthMeter = DefaultBandwidthMeter.Builder(context).build()
    private val trackSelector = DefaultTrackSelector(context)

    private var exoPlayer: ExoPlayer? = null
    private var totalDroppedFrames = 0
    private var detectedVideoCodec = "Detecting..."
    private var detectedAudioCodec = "Detecting..."

    private val _playerState = MutableStateFlow(PlayerState())
    override val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    init {
        initPlayer(BufferProfile.BALANCED)
        startPositionTicker()
    }

    fun getExoPlayer(): ExoPlayer? = exoPlayer

    private fun initPlayer(profile: BufferProfile) {
        val currentPos = exoPlayer?.currentPosition ?: 0L
        val currentPlayWhenReady = exoPlayer?.playWhenReady ?: false
        val currentMediaItem = exoPlayer?.currentMediaItem

        exoPlayer?.release()

        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                profile.minBufferMs,
                profile.maxBufferMs,
                profile.bufferForPlaybackMs,
                profile.bufferForPlaybackMs + 1000
            )
            .setPrioritizeTimeOverSizeThresholds(true)
            .build()

        val player = ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .setBandwidthMeter(bandwidthMeter)
            .setLoadControl(loadControl)
            .setMediaSourceFactory(DefaultMediaSourceFactory(context))
            .build()

        setupPlayerListeners(player)

        exoPlayer = player
        _playerState.update { it.copy(bufferProfile = profile) }

        if (currentMediaItem != null) {
            player.setMediaItem(currentMediaItem)
            player.prepare()
            if (currentPos > 0) player.seekTo(currentPos)
            player.playWhenReady = currentPlayWhenReady
        }
    }

    private fun setupPlayerListeners(player: ExoPlayer) {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                val isLoading = playbackState == Player.STATE_BUFFERING
                val isEnded = playbackState == Player.STATE_ENDED
                _playerState.update {
                    it.copy(
                        isLoading = isLoading,
                        durationMs = player.duration.coerceAtLeast(0L),
                        isPlaying = player.isPlaying
                    )
                }
                if (isEnded) {
                    _playerState.update { it.copy(isPlaying = false) }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playerState.update { it.copy(isPlaying = isPlaying) }
            }

            override fun onTracksChanged(tracks: Tracks) {
                parseTracks(tracks)
            }

            override fun onPlayerError(error: PlaybackException) {
                val errorMsg = error.localizedMessage ?: "Playback Error (${error.errorCodeName})"
                _playerState.update { it.copy(error = errorMsg, isLoading = false) }
            }

            override fun onVideoSizeChanged(videoSize: androidx.media3.common.VideoSize) {
                if (videoSize.width > 0 && videoSize.height > 0) {
                    _playerState.update { state ->
                        state.copy(
                            metrics = state.metrics.copy(
                                resolution = "${videoSize.width}x${videoSize.height}"
                            )
                        )
                    }
                }
            }
        })

        player.addAnalyticsListener(object : AnalyticsListener {
            override fun onDroppedVideoFrames(
                eventTime: AnalyticsListener.EventTime,
                droppedFrames: Int,
                elapsedMs: Long
            ) {
                totalDroppedFrames += droppedFrames
                _playerState.update { state ->
                    state.copy(
                        metrics = state.metrics.copy(
                            droppedFrames = totalDroppedFrames
                        )
                    )
                }
            }

            override fun onVideoDecoderInitialized(
                eventTime: AnalyticsListener.EventTime,
                decoderName: String,
                initializedTimestampMs: Long,
                initializationDurationMs: Long
            ) {
                detectedVideoCodec = decoderName
                _playerState.update { state ->
                    state.copy(
                        metrics = state.metrics.copy(videoCodec = decoderName)
                    )
                }
            }

            override fun onAudioDecoderInitialized(
                eventTime: AnalyticsListener.EventTime,
                decoderName: String,
                initializedTimestampMs: Long,
                initializationDurationMs: Long
            ) {
                detectedAudioCodec = decoderName
                _playerState.update { state ->
                    state.copy(
                        metrics = state.metrics.copy(audioCodec = decoderName)
                    )
                }
            }
        })
    }

    private fun parseTracks(tracks: Tracks) {
        val qualities = mutableListOf<QualityOption>()
        qualities.add(QualityOption.AUTO.copy(isSelected = _playerState.value.selectedQuality.isAuto))

        val audioTracks = mutableListOf<AudioTrackItem>()
        val subtitles = mutableListOf<SubtitleTrackItem>()
        subtitles.add(SubtitleTrackItem.OFF.copy(isSelected = _playerState.value.selectedSubtitle.id == "off"))

        for (group in tracks.groups) {
            when (group.type) {
                C.TRACK_TYPE_VIDEO -> {
                    for (i in 0 until group.length) {
                        val format = group.getTrackFormat(i)
                        val isSelected = group.isTrackSelected(i)
                        val height = format.height
                        val width = format.width
                        val bitrate = format.bitrate.toLong()
                        val codec = format.sampleMimeType ?: format.codecs ?: "Unknown"

                        if (height > 0) {
                            val label = "${height}p (${(bitrate / 1000)} kbps)"
                            qualities.add(
                                QualityOption(
                                    id = "$width x $height @ $bitrate",
                                    name = label,
                                    width = width,
                                    height = height,
                                    bitrate = bitrate,
                                    codec = codec,
                                    isSelected = isSelected && !_playerState.value.selectedQuality.isAuto
                                )
                            )
                        }
                    }
                }
                C.TRACK_TYPE_AUDIO -> {
                    for (i in 0 until group.length) {
                        val format = group.getTrackFormat(i)
                        val isSelected = group.isTrackSelected(i)
                        val lang = format.language ?: "und"
                        val label = format.label ?: lang
                        audioTracks.add(
                            AudioTrackItem(
                                id = format.id ?: "$lang-$i",
                                language = lang,
                                label = if (label != "und") label else "Audio track ${i + 1}",
                                isSelected = isSelected
                            )
                        )
                    }
                }
                C.TRACK_TYPE_TEXT -> {
                    for (i in 0 until group.length) {
                        val format = group.getTrackFormat(i)
                        val isSelected = group.isTrackSelected(i)
                        val lang = format.language ?: "und"
                        val label = format.label ?: lang
                        subtitles.add(
                            SubtitleTrackItem(
                                id = format.id ?: "$lang-$i",
                                language = lang,
                                label = if (label != "und") label else "Subtitle ${i + 1}",
                                isSelected = isSelected
                            )
                        )
                    }
                }
            }
        }

        _playerState.update { state ->
            state.copy(
                availableQualities = qualities.distinctBy { it.id },
                availableAudioTracks = audioTracks.distinctBy { it.id },
                availableSubtitles = subtitles.distinctBy { it.id },
                selectedAudioTrack = audioTracks.firstOrNull { it.isSelected } ?: state.selectedAudioTrack
            )
        }
    }

    private fun startPositionTicker() {
        tickerJob?.cancel()
        tickerJob = scope.launch {
            while (isActive) {
                exoPlayer?.let { player ->
                    val pos = player.currentPosition.coerceAtLeast(0L)
                    val dur = player.duration.coerceAtLeast(0L)
                    val buffered = player.bufferedPosition.coerceAtLeast(0L)
                    val bufferAheadMs = (buffered - pos).coerceAtLeast(0L)
                    val bandwidth = bandwidthMeter.bitrateEstimate
                    val percent = player.bufferedPercentage

                    // Find currently playing video format bitrate
                    var currentBitrate: Long = 0
                    for (group in player.currentTracks.groups) {
                        if (group.type == C.TRACK_TYPE_VIDEO) {
                            for (i in 0 until group.length) {
                                if (group.isTrackSelected(i)) {
                                    currentBitrate = group.getTrackFormat(i).bitrate.toLong()
                                }
                            }
                        }
                    }

                    _playerState.update { state ->
                        state.copy(
                            currentPositionMs = pos,
                            durationMs = dur,
                            bufferedPositionMs = buffered,
                            metrics = state.metrics.copy(
                                currentBitrateBps = if (currentBitrate > 0) currentBitrate else bandwidth,
                                estimatedBandwidthBps = bandwidth,
                                bufferDurationMs = bufferAheadMs,
                                bufferPercentage = percent,
                                videoCodec = detectedVideoCodec,
                                audioCodec = detectedAudioCodec
                            )
                        )
                    }
                }
                delay(300)
            }
        }
    }

    override fun onEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.LoadVideo -> loadVideo(event.video)
            PlayerEvent.Play -> exoPlayer?.play()
            PlayerEvent.Pause -> exoPlayer?.pause()
            PlayerEvent.TogglePlayPause -> {
                val player = exoPlayer ?: return
                if (player.isPlaying) player.pause() else player.play()
            }
            is PlayerEvent.SeekTo -> exoPlayer?.seekTo(event.positionMs)
            is PlayerEvent.SeekBy -> {
                val current = exoPlayer?.currentPosition ?: 0L
                val target = (current + event.deltaMs).coerceIn(0L, exoPlayer?.duration ?: 0L)
                exoPlayer?.seekTo(target)
            }
            is PlayerEvent.SetPlaybackSpeed -> {
                exoPlayer?.playbackParameters = PlaybackParameters(event.speed)
                _playerState.update { it.copy(playbackSpeed = event.speed) }
            }
            is PlayerEvent.SetResizeMode -> {
                _playerState.update { it.copy(resizeMode = event.mode) }
            }
            is PlayerEvent.SelectQuality -> selectQuality(event.quality)
            is PlayerEvent.SelectAudioTrack -> selectAudioTrack(event.track)
            is PlayerEvent.SelectSubtitle -> selectSubtitle(event.track)
            is PlayerEvent.SetBufferProfile -> {
                initPlayer(event.profile)
            }
            is PlayerEvent.SetVolume -> {
                exoPlayer?.volume = event.volume.coerceIn(0f, 1f)
                _playerState.update { it.copy(volume = event.volume, isMuted = event.volume == 0f) }
            }
            PlayerEvent.ToggleMute -> {
                val currentVolume = _playerState.value.volume
                val isMuted = _playerState.value.isMuted
                if (isMuted) {
                    exoPlayer?.volume = 1f
                    _playerState.update { it.copy(volume = 1f, isMuted = false) }
                } else {
                    exoPlayer?.volume = 0f
                    _playerState.update { it.copy(volume = 0f, isMuted = true) }
                }
            }
            PlayerEvent.ToggleStatsOverlay -> {
                _playerState.update { it.copy(isStatsOverlayVisible = !it.isStatsOverlayVisible) }
            }
            is PlayerEvent.SetFlagSecure -> {
                _playerState.update { it.copy(isFlagSecureEnabled = event.enabled) }
            }
            PlayerEvent.Replay -> {
                exoPlayer?.seekTo(0)
                exoPlayer?.play()
            }
            PlayerEvent.Release -> release()
        }
    }

    private fun loadVideo(video: Video) {
        totalDroppedFrames = 0
        detectedVideoCodec = "Detecting..."
        detectedAudioCodec = "Detecting..."
        _playerState.update {
            it.copy(
                currentVideo = video,
                error = null,
                isLoading = true,
                currentPositionMs = 0L,
                bufferedPositionMs = 0L,
                selectedQuality = QualityOption.AUTO,
                selectedSubtitle = SubtitleTrackItem.OFF
            )
        }

        val mediaItemBuilder = MediaItem.Builder()
            .setUri(Uri.parse(video.url))
            .setMediaId(video.id)

        // Configure DRM if stream specifies DRM protection
        video.drmConfig?.let { drm ->
            val uuid = when (drm.scheme) {
                DrmScheme.WIDEVINE -> C.WIDEVINE_UUID
                DrmScheme.CLEAR_KEY -> C.CLEARKEY_UUID
            }
            mediaItemBuilder.setDrmConfiguration(
                MediaItem.DrmConfiguration.Builder(uuid)
                    .setLicenseUri(drm.licenseUrl)
                    .build()
            )
        }

        val mediaItem = mediaItemBuilder.build()
        exoPlayer?.let { player ->
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
        }
    }

    private fun selectQuality(quality: QualityOption) {
        val player = exoPlayer ?: return
        val currentTracks = player.currentTracks

        if (quality.isAuto) {
            // Clear manual overrides to let Adaptive Bitrate (ABR) take over
            trackSelector.setParameters(
                trackSelector.buildUponParameters()
                    .clearOverridesOfType(C.TRACK_TYPE_VIDEO)
                    .clearVideoSizeConstraints()
                    .setMaxVideoBitrate(Int.MAX_VALUE)
            )
            _playerState.update { it.copy(selectedQuality = quality) }
            return
        }

        // Find matching track format and override
        for (group in currentTracks.groups) {
            if (group.type == C.TRACK_TYPE_VIDEO) {
                for (i in 0 until group.length) {
                    val format = group.getTrackFormat(i)
                    if (format.height == quality.height && (quality.bitrate == null || format.bitrate.toLong() == quality.bitrate)) {
                        val override = TrackSelectionOverride(group.mediaTrackGroup, i)
                        trackSelector.setParameters(
                            trackSelector.buildUponParameters()
                                .setOverrideForType(override)
                        )
                        _playerState.update { it.copy(selectedQuality = quality) }
                        return
                    }
                }
            }
        }
    }

    private fun selectAudioTrack(track: AudioTrackItem) {
        val player = exoPlayer ?: return
        for (group in player.currentTracks.groups) {
            if (group.type == C.TRACK_TYPE_AUDIO) {
                for (i in 0 until group.length) {
                    val format = group.getTrackFormat(i)
                    if ((format.id ?: "${format.language}-$i") == track.id) {
                        val override = TrackSelectionOverride(group.mediaTrackGroup, i)
                        trackSelector.setParameters(
                            trackSelector.buildUponParameters()
                                .setOverrideForType(override)
                        )
                        _playerState.update { it.copy(selectedAudioTrack = track) }
                        return
                    }
                }
            }
        }
    }

    private fun selectSubtitle(subtitle: SubtitleTrackItem) {
        if (subtitle.id == "off") {
            // Disable text tracks
            trackSelector.setParameters(
                trackSelector.buildUponParameters()
                    .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true)
            )
            _playerState.update { it.copy(selectedSubtitle = subtitle) }
            return
        }

        val player = exoPlayer ?: return
        trackSelector.setParameters(
            trackSelector.buildUponParameters()
                .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, false)
        )

        for (group in player.currentTracks.groups) {
            if (group.type == C.TRACK_TYPE_TEXT) {
                for (i in 0 until group.length) {
                    val format = group.getTrackFormat(i)
                    if ((format.id ?: "${format.language}-$i") == subtitle.id) {
                        val override = TrackSelectionOverride(group.mediaTrackGroup, i)
                        trackSelector.setParameters(
                            trackSelector.buildUponParameters()
                                .setOverrideForType(override)
                        )
                        _playerState.update { it.copy(selectedSubtitle = subtitle) }
                        return
                    }
                }
            }
        }
    }

    override fun release() {
        tickerJob?.cancel()
        scope.cancel()
        exoPlayer?.release()
        exoPlayer = null
    }
}
