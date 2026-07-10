package com.cbhard.myvideoplayer.data

import jakarta.inject.Inject

class VideoRepositoryImpl @Inject constructor() :
    VideoRepository {

    override suspend fun getVideos(): List<Video> {

        return listOf(

            Video(
                id = "1",
                title = "Big Buck Bunny",
                url = "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"
            ),

            Video(
                id = "2",
                title = "Tears of Steel",
                url = "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3"
            ),

            Video(
                id = "3",
                title = "HLS Demo",
                url = "https://storage.googleapis.com/shaka-demo-assets/angel-one-hls/hls.m3u8"
            )
        )
    }

}