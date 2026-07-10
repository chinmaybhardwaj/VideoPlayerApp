package com.cbhard.myvideoplayer.player

data class PlayerState(

    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val duration: Long = 0,
    val currentPosition: Long = 0,
    val bufferedPercentage: Int = 0,
    val currentMediaIndex: Int = 0,
    val error: String? = null
)