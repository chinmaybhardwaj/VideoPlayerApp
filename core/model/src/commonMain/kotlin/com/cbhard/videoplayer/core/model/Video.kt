package com.cbhard.videoplayer.core.model

enum class VideoCategory(val displayName: String, val description: String) {
    ADAPTIVE_ABR(
        displayName = "Adaptive Streaming (ABR)",
        description = "Multi-bitrate HLS and DASH streams that dynamically adapt to bandwidth"
    ),
    CODECS(
        displayName = "Codec Showcase",
        description = "Side-by-side comparison of modern codecs: AV1, HEVC (H.265), and AVC (H.264)"
    ),
    MULTI_TRACK(
        displayName = "Multi-Track Audio & Subs",
        description = "Streams with multiple audio languages and WebVTT subtitle tracks"
    ),
    DRM(
        displayName = "DRM Content Protection",
        description = "Widevine & ClearKey encrypted playback demonstrating digital rights management"
    )
}

enum class DrmScheme {
    WIDEVINE,
    CLEAR_KEY
}

data class DrmConfig(
    val scheme: DrmScheme,
    val licenseUrl: String,
    val keyIdHex: String? = null,
    val keyHex: String? = null
)

data class Video(
    val id: String,
    val title: String,
    val url: String,
    val category: VideoCategory,
    val description: String,
    val thumbnailUrl: String = "",
    val codecInfo: String? = null,
    val drmConfig: DrmConfig? = null
)
