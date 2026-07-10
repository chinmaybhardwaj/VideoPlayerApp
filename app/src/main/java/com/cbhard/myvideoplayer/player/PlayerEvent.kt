package com.cbhard.myvideoplayer.player

sealed interface PlayerEvent

data object Play : PlayerEvent

data object Pause : PlayerEvent

data object Toggle : PlayerEvent

data object Replay : PlayerEvent

data object Next : PlayerEvent

data object Previous : PlayerEvent

data class SeekTo(
    val position: Long
) : PlayerEvent

data class ChangeSpeed(
    val speed: Float
) : PlayerEvent