package com.cbhard.videoplayer.composeapp.ui

import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.cbhard.videoplayer.core.model.ResizeMode
import com.cbhard.videoplayer.core.player.AndroidPlayerController
import com.cbhard.videoplayer.core.player.PlayerController

@OptIn(UnstableApi::class)
@Composable
actual fun PlatformPlayerSurface(
    playerController: PlayerController,
    modifier: Modifier
) {
    val androidController = playerController as? AndroidPlayerController
    val exoPlayer = androidController?.getExoPlayer()
    val playerState by playerController.playerState.collectAsStateWithLifecycle()

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                useController = false
                player = exoPlayer
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        update = { view ->
            view.player = exoPlayer
            view.resizeMode = when (playerState.resizeMode) {
                ResizeMode.FIT -> AspectRatioFrameLayout.RESIZE_MODE_FIT
                ResizeMode.FILL -> AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                ResizeMode.FIXED_16_9 -> AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                ResizeMode.STRETCH -> AspectRatioFrameLayout.RESIZE_MODE_FILL
            }
        },
        modifier = modifier.fillMaxSize()
    )
}
