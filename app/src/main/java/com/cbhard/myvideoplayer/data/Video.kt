package com.cbhard.myvideoplayer.data

data class Video(

    val id: String,
    val title: String,
    val url: String,
    val thumbnail: String = ""
)