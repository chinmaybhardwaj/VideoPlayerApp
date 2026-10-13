package com.cbhard.videoplayer.core.player

import com.cbhard.videoplayer.core.model.BufferProfile
import com.cbhard.videoplayer.core.model.PlayerMetrics
import com.cbhard.videoplayer.core.model.QualityOption
import com.cbhard.videoplayer.core.model.ResizeMode
import com.cbhard.videoplayer.core.model.SubtitleTrackItem
import com.cbhard.videoplayer.core.model.Video
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
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
import platform.AVFAudio.*
import platform.AVFoundation.*
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSURL
import platform.darwin.NSObject

/**
 * Native iOS AVPlayer engine implementation of [PlayerController].
 * Interfaces directly with Apple AVFoundation and CoreMedia.
 */
@OptIn(ExperimentalForeignApi::class)
class IosPlayerController : PlayerController {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var tickerJob: Job? = null

    private var avPlayer: AVPlayer? = null
    private var endObserver: NSObject? = null

    private val _playerState = MutableStateFlow(PlayerState())
    override val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    init {
        configureAudioSession()
        avPlayer = AVPlayer()
        startPositionTicker()
    }

    fun getAVPlayer(): AVPlayer? = avPlayer

    private fun configureAudioSession() {
        try {
            val session = AVAudioSession.sharedInstance()
            session.setCategory(AVAudioSessionCategoryPlayback, error = null)
            session.setActive(true, error = null)
        } catch (_: Exception) {
            // Audio session non-fatal fallback
        }
    }

    override fun onEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.LoadVideo -> loadVideo(event.video)
            PlayerEvent.Play -> {
                avPlayer?.play()
                _playerState.update { it.copy(isPlaying = true) }
            }
            PlayerEvent.Pause -> {
                avPlayer?.pause()
                _playerState.update { it.copy(isPlaying = false) }
            }
            PlayerEvent.TogglePlayPause -> {
                val player = avPlayer ?: return
                if (player.timeControlStatus == AVPlayerTimeControlStatusPlaying) {
                    player.pause()
                    _playerState.update { it.copy(isPlaying = false) }
                } else {
                    player.play()
                    _playerState.update { it.copy(isPlaying = true) }
                }
            }
            is PlayerEvent.SeekTo -> {
                val seconds = event.positionMs / 1000.0
                val targetTime = CMTimeMakeWithSeconds(seconds, preferredTimescale = 600)
                avPlayer?.seekToTime(targetTime)
                _playerState.update { it.copy(currentPositionMs = event.positionMs) }
            }
            is PlayerEvent.SeekBy -> {
                val currentMs = _playerState.value.currentPositionMs
                val durationMs = _playerState.value.durationMs
                val targetMs = (currentMs + event.deltaMs).coerceIn(0L, if (durationMs > 0) durationMs else Long.MAX_VALUE)
                val seconds = targetMs / 1000.0
                val targetTime = CMTimeMakeWithSeconds(seconds, preferredTimescale = 600)
                avPlayer?.seekToTime(targetTime)
                _playerState.update { it.copy(currentPositionMs = targetMs) }
            }
            is PlayerEvent.SetPlaybackSpeed -> {
                avPlayer?.setRate(event.speed)
                _playerState.update { it.copy(playbackSpeed = event.speed) }
            }
            is PlayerEvent.SetResizeMode -> {
                _playerState.update { it.copy(resizeMode = event.mode) }
            }
            is PlayerEvent.SelectQuality -> {
                _playerState.update { it.copy(selectedQuality = event.quality) }
            }
            is PlayerEvent.SelectAudioTrack -> {
                _playerState.update { it.copy(selectedAudioTrack = event.track) }
            }
            is PlayerEvent.SelectSubtitle -> {
                _playerState.update { it.copy(selectedSubtitle = event.track) }
            }
            is PlayerEvent.SetBufferProfile -> {
                _playerState.update { it.copy(bufferProfile = event.profile) }
            }
            is PlayerEvent.SetVolume -> {
                avPlayer?.setVolume(event.volume)
                _playerState.update { it.copy(volume = event.volume, isMuted = event.volume == 0f) }
            }
            PlayerEvent.ToggleMute -> {
                val player = avPlayer ?: return
                val newMute = !player.isMuted()
                player.setMuted(newMute)
                _playerState.update { it.copy(isMuted = newMute) }
            }
            PlayerEvent.ToggleStatsOverlay -> {
                _playerState.update { it.copy(isStatsOverlayVisible = !it.isStatsOverlayVisible) }
            }
            is PlayerEvent.SetFlagSecure -> {
                _playerState.update { it.copy(isFlagSecureEnabled = event.enabled) }
            }
            PlayerEvent.Replay -> {
                val zeroTime = CMTimeMakeWithSeconds(0.0, preferredTimescale = 600)
                avPlayer?.seekToTime(zeroTime)
                avPlayer?.play()
                _playerState.update { it.copy(currentPositionMs = 0L, isPlaying = true) }
            }
            PlayerEvent.Release -> release()
        }
    }

    private fun loadVideo(video: Video) {
        val nsUrl = NSURL.URLWithString(video.url) ?: return
        val playerItem = AVPlayerItem(uRL = nsUrl)

        // Observe playback completion
        endObserver?.let { NSNotificationCenter.defaultCenter.removeObserver(it) }
        endObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            `object` = playerItem,
            queue = NSOperationQueue.mainQueue
        ) { _ ->
            _playerState.update { it.copy(isPlaying = false) }
        } as? NSObject

        avPlayer?.replaceCurrentItemWithPlayerItem(playerItem)
        avPlayer?.play()

        _playerState.update {
            it.copy(
                currentVideo = video,
                isPlaying = true,
                isLoading = true,
                currentPositionMs = 0L,
                bufferedPositionMs = 0L,
                error = null,
                metrics = PlayerMetrics(
                    resolution = "Loading...",
                    videoCodec = video.codecInfo ?: "AVFoundation HLS/H.264",
                    audioCodec = "AAC / CoreAudio",
                    estimatedBandwidthBps = 15_000_000L
                )
            )
        }
    }

    private fun startPositionTicker() {
        tickerJob?.cancel()
        tickerJob = scope.launch {
            while (isActive) {
                updatePlaybackState()
                delay(250)
            }
        }
    }

    private fun updatePlaybackState() {
        val player = avPlayer ?: return
        val currentItem = player.currentItem ?: return

        val isPlaying = player.timeControlStatus == AVPlayerTimeControlStatusPlaying
        val isLoading = currentItem.status != AVPlayerItemStatusReadyToPlay && currentItem.status != AVPlayerItemStatusFailed

        // Extract Current Time
        val currentTimeSec = CMTimeGetSeconds(player.currentTime())
        val currentPositionMs = if (!currentTimeSec.isNaN() && !currentTimeSec.isInfinite() && currentTimeSec >= 0) {
            (currentTimeSec * 1000).toLong()
        } else 0L

        // Extract Duration
        val durationSec = CMTimeGetSeconds(currentItem.duration)
        val durationMs = if (!durationSec.isNaN() && !durationSec.isInfinite() && durationSec > 0) {
            (durationSec * 1000).toLong()
        } else 0L

        // Extract Access Log Metrics for "Stats for Nerds"
        val accessLogEvent = currentItem.accessLog()?.events?.lastOrNull() as? AVPlayerItemAccessLogEvent
        val bitrate = (accessLogEvent?.indicatedBitrate ?: accessLogEvent?.observedBitrate ?: 0.0).toLong()
        val droppedFrames = (accessLogEvent?.numberOfDroppedVideoFrames?.toInt()) ?: 0

        val presentationSize = currentItem.presentationSize
        val resolutionStr = presentationSize.useContents {
            if (width > 0 && height > 0) "${width.toInt()}x${height.toInt()}" else _playerState.value.metrics.resolution
        }

        val bufferRemainingSec = if (durationSec > currentTimeSec) (durationSec - currentTimeSec) else 0.0
        val bufferPercentage = if (durationSec > 0) ((currentTimeSec / durationSec) * 100).toInt().coerceIn(0, 100) else 0

        _playerState.update { state ->
            state.copy(
                isPlaying = isPlaying,
                isLoading = isLoading,
                currentPositionMs = currentPositionMs,
                durationMs = if (durationMs > 0) durationMs else state.durationMs,
                bufferedPositionMs = (currentPositionMs + (bufferRemainingSec * 1000).toLong()).coerceAtMost(durationMs),
                metrics = state.metrics.copy(
                    resolution = resolutionStr,
                    currentBitrateBps = if (bitrate > 0) bitrate else state.metrics.currentBitrateBps,
                    estimatedBandwidthBps = if (bitrate > 0) (bitrate * 1.5).toLong() else state.metrics.estimatedBandwidthBps,
                    bufferDurationMs = (bufferRemainingSec * 1000).toLong(),
                    bufferPercentage = bufferPercentage,
                    droppedFrames = droppedFrames
                )
            )
        }
    }

    override fun release() {
        tickerJob?.cancel()
        endObserver?.let { NSNotificationCenter.defaultCenter.removeObserver(it) }
        avPlayer?.pause()
        avPlayer?.replaceCurrentItemWithPlayerItem(null)
        avPlayer = null
        scope.cancel()
    }
}
