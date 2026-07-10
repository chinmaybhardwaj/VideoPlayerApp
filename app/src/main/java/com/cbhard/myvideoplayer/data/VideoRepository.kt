package com.cbhard.myvideoplayer.data


interface VideoRepository {

    suspend fun getVideos(): List<Video>

}