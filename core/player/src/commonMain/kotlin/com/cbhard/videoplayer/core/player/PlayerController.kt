package com.cbhard.videoplayer.core.player

import kotlinx.coroutines.flow.StateFlow

interface PlayerController {
    val playerState: StateFlow<PlayerState>
    fun onEvent(event: PlayerEvent)
    fun release()
}
