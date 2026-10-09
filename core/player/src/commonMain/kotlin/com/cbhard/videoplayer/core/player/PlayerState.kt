package com.cbhard.videoplayer.core.player

import com.cbhard.videoplayer.core.model.AudioTrackItem
import com.cbhard.videoplayer.core.model.BufferProfile
import com.cbhard.videoplayer.core.model.PlayerMetrics
import com.cbhard.videoplayer.core.model.QualityOption
import com.cbhard.videoplayer.core.model.ResizeMode
import com.cbhard.videoplayer.core.model.SubtitleTrackItem
import com.cbhard.videoplayer.core.model.Video

data class PlayerState(
    val currentVideo: Video? = null,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val durationMs: Long = 0L,
    val currentPositionMs: Long = 0L,
    val bufferedPositionMs: Long = 0L,
    val playbackSpeed: Float = 1.0f,
    val resizeMode: ResizeMode = ResizeMode.FIT,
    val availableQualities: List<QualityOption> = listOf(QualityOption.AUTO),
    val selectedQuality: QualityOption = QualityOption.AUTO,
    val availableAudioTracks: List<AudioTrackItem> = emptyList(),
    val selectedAudioTrack: AudioTrackItem? = null,
    val availableSubtitles: List<SubtitleTrackItem> = listOf(SubtitleTrackItem.OFF),
    val selectedSubtitle: SubtitleTrackItem = SubtitleTrackItem.OFF,
    val bufferProfile: BufferProfile = BufferProfile.BALANCED,
    val volume: Float = 1.0f,
    val isMuted: Boolean = false,
    val metrics: PlayerMetrics = PlayerMetrics(),
    val isStatsOverlayVisible: Boolean = false,
    val isFlagSecureEnabled: Boolean = true,
    val isScreenRecordingDetected: Boolean = false,
    val error: String? = null
) {
    val progress: Float
        get() = if (durationMs > 0) (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f) else 0f

    val bufferedProgress: Float
        get() = if (durationMs > 0) (bufferedPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f) else 0f
}
