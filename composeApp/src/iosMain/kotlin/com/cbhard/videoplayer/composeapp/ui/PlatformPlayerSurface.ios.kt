package com.cbhard.videoplayer.composeapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.cbhard.videoplayer.core.player.PlayerController

@Composable
actual fun PlatformPlayerSurface(
    playerController: PlayerController,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text("iOS AVPlayer Surface", color = Color.White)
    }
}
