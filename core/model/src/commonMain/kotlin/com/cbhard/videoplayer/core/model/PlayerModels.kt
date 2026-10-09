package com.cbhard.videoplayer.core.model

data class QualityOption(
    val id: String,
    val name: String,
    val width: Int? = null,
    val height: Int? = null,
    val bitrate: Long? = null,
    val codec: String? = null,
    val isAuto: Boolean = false,
    val isSelected: Boolean = false
) {
    companion object {
        val AUTO = QualityOption(
            id = "auto",
            name = "Auto (Adaptive)",
            isAuto = true,
            isSelected = true
        )
    }
}

data class AudioTrackItem(
    val id: String,
    val language: String,
    val label: String,
    val isSelected: Boolean = false
)

data class SubtitleTrackItem(
    val id: String,
    val language: String,
    val label: String,
    val isSelected: Boolean = false
) {
    companion object {
        val OFF = SubtitleTrackItem(
            id = "off",
            language = "",
            label = "Off",
            isSelected = true
        )
    }
}

enum class BufferProfile(
    val title: String,
    val subtitle: String,
    val minBufferMs: Int,
    val maxBufferMs: Int,
    val bufferForPlaybackMs: Int
) {
    AGGRESSIVE(
        title = "Quick Start",
        subtitle = "Lowest startup latency, minimal caching (2.5s-10s)",
        minBufferMs = 2500,
        maxBufferMs = 10000,
        bufferForPlaybackMs = 1000
    ),
    BALANCED(
        title = "Balanced (OTT Default)",
        subtitle = "Standard Netflix/YouTube style buffering (15s-50s)",
        minBufferMs = 15000,
        maxBufferMs = 50000,
        bufferForPlaybackMs = 2500
    ),
    HIGH_RESILIENCE(
        title = "Deep Buffer (Smooth)",
        subtitle = "Maximizes buffer ahead for unstable connections (30s-120s)",
        minBufferMs = 30000,
        maxBufferMs = 120000,
        bufferForPlaybackMs = 5000
    )
}

enum class ResizeMode(val label: String) {
    FIT("Fit (Aspect Ratio)"),
    FILL("Fill (Crop)"),
    FIXED_16_9("16:9"),
    STRETCH("Stretch")
}

data class PlayerMetrics(
    val resolution: String = "Unknown",
    val currentBitrateBps: Long = 0,
    val estimatedBandwidthBps: Long = 0,
    val bufferDurationMs: Long = 0,
    val videoCodec: String = "Detecting...",
    val audioCodec: String = "Detecting...",
    val droppedFrames: Int = 0,
    val fps: Float = 0f,
    val bufferPercentage: Int = 0
)
