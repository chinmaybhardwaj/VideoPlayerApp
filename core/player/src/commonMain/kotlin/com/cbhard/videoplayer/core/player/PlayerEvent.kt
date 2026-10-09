package com.cbhard.videoplayer.core.player

import com.cbhard.videoplayer.core.model.AudioTrackItem
import com.cbhard.videoplayer.core.model.BufferProfile
import com.cbhard.videoplayer.core.model.QualityOption
import com.cbhard.videoplayer.core.model.ResizeMode
import com.cbhard.videoplayer.core.model.SubtitleTrackItem
import com.cbhard.videoplayer.core.model.Video

sealed interface PlayerEvent {
    data class LoadVideo(val video: Video) : PlayerEvent
    data object Play : PlayerEvent
    data object Pause : PlayerEvent
    data object TogglePlayPause : PlayerEvent
    data class SeekTo(val positionMs: Long) : PlayerEvent
    data class SeekBy(val deltaMs: Long) : PlayerEvent
    data class SetPlaybackSpeed(val speed: Float) : PlayerEvent
    data class SetResizeMode(val mode: ResizeMode) : PlayerEvent
    data class SelectQuality(val quality: QualityOption) : PlayerEvent
    data class SelectAudioTrack(val track: AudioTrackItem) : PlayerEvent
    data class SelectSubtitle(val track: SubtitleTrackItem) : PlayerEvent
    data class SetBufferProfile(val profile: BufferProfile) : PlayerEvent
    data class SetVolume(val volume: Float) : PlayerEvent
    data object ToggleMute : PlayerEvent
    data object ToggleStatsOverlay : PlayerEvent
    data class SetFlagSecure(val enabled: Boolean) : PlayerEvent
    data object Replay : PlayerEvent
    data object Release : PlayerEvent
}
