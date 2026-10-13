package com.cbhard.videoplayer.composeapp.ui

import androidx.lifecycle.ViewModel
import com.cbhard.videoplayer.core.player.PlayerController
import com.cbhard.videoplayer.core.player.PlayerEvent
import com.cbhard.videoplayer.core.player.PlayerState
import kotlinx.coroutines.flow.StateFlow

class PlayerViewModel(
    val playerController: PlayerController
) : ViewModel() {

    val playerState: StateFlow<PlayerState> = playerController.playerState

    fun onEvent(event: PlayerEvent) {
        playerController.onEvent(event)
    }
}
