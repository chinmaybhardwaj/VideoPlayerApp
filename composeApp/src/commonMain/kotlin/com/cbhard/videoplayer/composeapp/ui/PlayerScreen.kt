package com.cbhard.videoplayer.composeapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cbhard.videoplayer.composeapp.ui.controls.PlayerControlsOverlay
import com.cbhard.videoplayer.core.player.PlayerController
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlayerScreen(
    playerController: PlayerController,
    onBackClick: () -> Unit,
    onPictureInPictureClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = koinViewModel()
) {
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()

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
            onEvent = { event -> viewModel.onEvent(event) },
            onBackClick = onBackClick,
            onPictureInPictureClick = onPictureInPictureClick,
            modifier = Modifier.fillMaxSize()
        )
    }
}
