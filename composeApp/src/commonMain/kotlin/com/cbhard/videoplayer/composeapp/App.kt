package com.cbhard.videoplayer.composeapp

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.cbhard.videoplayer.composeapp.data.VideoRepository
import com.cbhard.videoplayer.composeapp.ui.CatalogScreen
import com.cbhard.videoplayer.composeapp.ui.PlayerScreen
import com.cbhard.videoplayer.core.designsystem.VideoPlayerTheme
import com.cbhard.videoplayer.core.player.PlayerController
import com.cbhard.videoplayer.core.player.PlayerEvent
import org.koin.compose.koinInject

enum class AppDestination {
    CATALOG,
    PLAYER
}

@Composable
fun App(
    onPictureInPictureClick: () -> Unit = {},
    playerController: PlayerController = koinInject(),
    videoRepository: VideoRepository = koinInject(),
    modifier: Modifier = Modifier
) {
    var currentScreen by remember { mutableStateOf(AppDestination.CATALOG) }

    VideoPlayerTheme {
        Crossfade(
            targetState = currentScreen,
            modifier = modifier.fillMaxSize()
        ) { destination ->
            when (destination) {
                AppDestination.CATALOG -> {
                    CatalogScreen(
                        videos = videoRepository.getVideos(),
                        onVideoSelected = { video ->
                            playerController.onEvent(PlayerEvent.LoadVideo(video))
                            currentScreen = AppDestination.PLAYER
                        }
                    )
                }

                AppDestination.PLAYER -> {
                    PlayerScreen(
                        playerController = playerController,
                        onBackClick = {
                            playerController.onEvent(PlayerEvent.Pause)
                            currentScreen = AppDestination.CATALOG
                        },
                        onPictureInPictureClick = onPictureInPictureClick
                    )
                }
            }
        }
    }
}
