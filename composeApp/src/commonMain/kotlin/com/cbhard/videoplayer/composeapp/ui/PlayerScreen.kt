package com.cbhard.videoplayer.composeapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.cbhard.videoplayer.core.designsystem.PlayerControlsOverlay
import com.cbhard.videoplayer.core.player.PlayerController

@Composable
fun PlayerScreen(
    playerController: PlayerController,
    onBackClick: () -> Unit,
    onPictureInPictureClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val playerState by playerController.playerState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Platform Native Video Engine Surface (Media3 PlayerView on Android / AVPlayer on iOS)
        PlatformPlayerSurface(
            playerController = playerController,
            modifier = Modifier.fillMaxSize()
        )

        // Compose Multiplatform OTT Controls, Gestures & Telemetry Overlay
        PlayerControlsOverlay(
            playerState = playerState,
            onEvent = { event -> playerController.onEvent(event) },
            onBackClick = onBackClick,
            onPictureInPictureClick = onPictureInPictureClick,
            modifier = Modifier.fillMaxSize()
        )
    }
}
