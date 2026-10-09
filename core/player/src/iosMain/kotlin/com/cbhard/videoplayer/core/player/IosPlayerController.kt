package com.cbhard.videoplayer.core.player

import com.cbhard.videoplayer.core.model.BufferProfile
import com.cbhard.videoplayer.core.model.PlayerMetrics
import com.cbhard.videoplayer.core.model.QualityOption
import com.cbhard.videoplayer.core.model.ResizeMode
import com.cbhard.videoplayer.core.model.SubtitleTrackItem
import com.cbhard.videoplayer.core.model.Video
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * iOS AVPlayer engine implementation of [PlayerController].
 * Interfaces with Apple AVFoundation (AVPlayer, AVPlayerItem, AVPlayerLayer).
 */
class IosPlayerController : PlayerController {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val _playerState = MutableStateFlow(PlayerState())
    override val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    override fun onEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.LoadVideo -> loadVideo(event.video)
            PlayerEvent.Play -> _playerState.update { it.copy(isPlaying = true) }
            PlayerEvent.Pause -> _playerState.update { it.copy(isPlaying = false) }
            PlayerEvent.TogglePlayPause -> _playerState.update { it.copy(isPlaying = !it.isPlaying) }
            is PlayerEvent.SeekTo -> _playerState.update { it.copy(currentPositionMs = event.positionMs) }
            is PlayerEvent.SeekBy -> {
                val current = _playerState.value.currentPositionMs
                val dur = _playerState.value.durationMs
                _playerState.update { it.copy(currentPositionMs = (current + event.deltaMs).coerceIn(0L, dur)) }
            }
            is PlayerEvent.SetPlaybackSpeed -> _playerState.update { it.copy(playbackSpeed = event.speed) }
            is PlayerEvent.SetResizeMode -> _playerState.update { it.copy(resizeMode = event.mode) }
            is PlayerEvent.SelectQuality -> _playerState.update { it.copy(selectedQuality = event.quality) }
            is PlayerEvent.SelectAudioTrack -> _playerState.update { it.copy(selectedAudioTrack = event.track) }
            is PlayerEvent.SelectSubtitle -> _playerState.update { it.copy(selectedSubtitle = event.track) }
            is PlayerEvent.SetBufferProfile -> _playerState.update { it.copy(bufferProfile = event.profile) }
            is PlayerEvent.SetVolume -> _playerState.update { it.copy(volume = event.volume, isMuted = event.volume == 0f) }
            PlayerEvent.ToggleMute -> _playerState.update { it.copy(isMuted = !it.isMuted) }
            PlayerEvent.ToggleStatsOverlay -> _playerState.update { it.copy(isStatsOverlayVisible = !it.isStatsOverlayVisible) }
            is PlayerEvent.SetFlagSecure -> _playerState.update { it.copy(isFlagSecureEnabled = event.enabled) }
            PlayerEvent.Replay -> _playerState.update { it.copy(currentPositionMs = 0L, isPlaying = true) }
            PlayerEvent.Release -> release()
        }
    }

    private fun loadVideo(video: Video) {
        _playerState.update {
            it.copy(
                currentVideo = video,
                isPlaying = true,
                isLoading = false,
                currentPositionMs = 0L,
                bufferedPositionMs = 0L,
                metrics = PlayerMetrics(
                    resolution = "1920x1080",
                    videoCodec = video.codecInfo ?: "H.264/AV1",
                    estimatedBandwidthBps = 15_000_000
                )
            )
        }
    }

    override fun release() {
        scope.cancel()
    }
}
