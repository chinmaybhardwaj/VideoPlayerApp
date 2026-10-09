package com.cbhard.videoplayer.composeapp.data

import com.cbhard.videoplayer.core.model.DrmConfig
import com.cbhard.videoplayer.core.model.DrmScheme
import com.cbhard.videoplayer.core.model.Video
import com.cbhard.videoplayer.core.model.VideoCategory

interface VideoRepository {
    fun getVideos(): List<Video>
    fun getVideosByCategory(category: VideoCategory): List<Video>
    fun getVideoById(id: String): Video?
}

class VideoRepositoryImpl : VideoRepository {

    private val videos = listOf(
        // 1. ADAPTIVE STREAMING (ABR)
        Video(
            id = "abr-hls-mux",
            title = "Big Buck Bunny - Multi-bitrate HLS",
            url = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8",
            category = VideoCategory.ADAPTIVE_ABR,
            description = "Adaptive HTTP Live Streaming (HLS) with multiple renditions (1080p down to 360p) dynamically adapting to network bandwidth.",
            codecInfo = "HLS • H.264 ABR • Auto / 1080p / 720p / 480p / 360p"
        ),
        Video(
            id = "abr-dash-akamai",
            title = "Big Buck Bunny - Multi-bitrate DASH",
            url = "https://dash.akamaized.net/akamai/bbb_30fps/bbb_30fps.mpd",
            category = VideoCategory.ADAPTIVE_ABR,
            description = "Dynamic Adaptive Streaming over HTTP (DASH) demonstrating Media3 native chunk parser and bandwidth estimation.",
            codecInfo = "DASH • MPEG-DASH MPD • Multi-bitrate"
        ),
        Video(
            id = "abr-angel-one",
            title = "Angel One - Adaptive HLS",
            url = "https://storage.googleapis.com/shaka-demo-assets/angel-one-hls/hls.m3u8",
            category = VideoCategory.ADAPTIVE_ABR,
            description = "Multi-bitrate HLS stream from Google Shaka demo assets featuring diverse audio channels.",
            codecInfo = "HLS • Multi-rendition • AAC"
        ),

        // 2. CODECS SHOWCASE
        Video(
            id = "codec-av1",
            title = "AV1 Codec Showcase (1080p)",
            url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            category = VideoCategory.CODECS,
            description = "Next-generation royalty-free AV1 (AOMedia Video 1) video compression offering ~30% higher compression efficiency than HEVC.",
            codecInfo = "AV1 (video/av01) • 1080p@30fps • High Compression"
        ),
        Video(
            id = "codec-hevc",
            title = "HEVC / H.265 Showcase (1080p)",
            url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            category = VideoCategory.CODECS,
            description = "High Efficiency Video Coding (HEVC/H.265) hardware accelerated decoding test for ultra-efficient playback.",
            codecInfo = "HEVC / H.265 (video/hevc) • 1080p • Hardware Accelerated"
        ),
        Video(
            id = "codec-h264",
            title = "H.264 / AVC Showcase (1080p)",
            url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            category = VideoCategory.CODECS,
            description = "Industry standard Advanced Video Coding (AVC/H.264) baseline compatible with every modern device hardware decoder.",
            codecInfo = "H.264 / AVC (video/avc) • 1080p • Universal Compatibility"
        ),

        // 3. MULTI-TRACK AUDIO & SUBTITLES
        Video(
            id = "multi-sintel",
            title = "Sintel - Multi-Language Audio & WebVTT",
            url = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
            category = VideoCategory.MULTI_TRACK,
            description = "Features alternate audio streams (English, German, Spanish, French) and multiple WebVTT closed-caption tracks.",
            codecInfo = "HLS • Multi-Audio (EN/DE/ES/FR) • WebVTT CC"
        ),
        Video(
            id = "multi-tears-steel",
            title = "Tears of Steel - Multi-Language Subtitles",
            url = "https://storage.googleapis.com/shaka-demo-assets/tears-of-steel/tearsofsteel.mpd",
            category = VideoCategory.MULTI_TRACK,
            description = "DASH stream with multiple subtitle languages for testing ExoPlayer TrackSelector text track switching.",
            codecInfo = "DASH • Multi-Subtitles • High Definition"
        ),

        // 4. DRM & SECURITY
        Video(
            id = "drm-clearkey",
            title = "ClearKey Encrypted Stream (DRM Demo)",
            url = "https://storage.googleapis.com/shaka-demo-assets/angel-one-widevine/dash.mpd",
            category = VideoCategory.DRM,
            description = "Encrypted DASH stream protected with Widevine/ClearKey DRM. Demonstrates key exchange and hardware-protected frame buffers.",
            codecInfo = "DASH DRM • ClearKey / Widevine • Encrypted Media Extensions",
            drmConfig = DrmConfig(
                scheme = DrmScheme.CLEAR_KEY,
                licenseUrl = "https://cwip-shaka-drm.appspot.com/no_auth"
            )
        )
    )

    override fun getVideos(): List<Video> = videos

    override fun getVideosByCategory(category: VideoCategory): List<Video> =
        videos.filter { it.category == category }

    override fun getVideoById(id: String): Video? =
        videos.find { it.id == id }
}
