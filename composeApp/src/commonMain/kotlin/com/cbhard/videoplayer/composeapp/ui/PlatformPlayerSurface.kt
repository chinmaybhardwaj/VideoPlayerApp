package com.cbhard.videoplayer.composeapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cbhard.videoplayer.core.player.PlayerController

@Composable
expect fun PlatformPlayerSurface(
    playerController: PlayerController,
    modifier: Modifier = Modifier
)
