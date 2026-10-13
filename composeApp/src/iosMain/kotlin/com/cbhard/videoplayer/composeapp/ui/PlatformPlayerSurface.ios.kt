package com.cbhard.videoplayer.composeapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import com.cbhard.videoplayer.core.model.ResizeMode
import com.cbhard.videoplayer.core.player.IosPlayerController
import com.cbhard.videoplayer.core.player.PlayerController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import platform.AVFoundation.AVLayerVideoGravity
import platform.AVFoundation.AVLayerVideoGravityResize
import platform.AVFoundation.AVLayerVideoGravityResizeAspect
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerLayer
import platform.CoreGraphics.CGRect
import platform.QuartzCore.CATransaction
import platform.UIKit.UIColor
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
private class PlayerUIView : UIView(frame = cValue<CGRect> {
    origin.x = 0.0
    origin.y = 0.0
    size.width = 0.0
    size.height = 0.0
}) {
    private val playerLayer = AVPlayerLayer()

    init {
        backgroundColor = UIColor.blackColor
        layer.addSublayer(playerLayer)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        CATransaction.begin()
        CATransaction.setDisableActions(true)
        playerLayer.frame = bounds
        CATransaction.commit()
    }

    fun setPlayer(player: AVPlayer?) {
        playerLayer.player = player
    }

    fun setVideoGravity(gravity: AVLayerVideoGravity) {
        playerLayer.videoGravity = gravity
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PlatformPlayerSurface(
    playerController: PlayerController,
    modifier: Modifier
) {
    val iosController = playerController as? IosPlayerController
    val avPlayer = iosController?.getAVPlayer()
    val playerState by playerController.playerState.collectAsState()

    UIKitView(
        factory = {
            PlayerUIView().apply {
                setPlayer(avPlayer)
            }
        },
        update = { view ->
            (view as? PlayerUIView)?.let { pv ->
                pv.setPlayer(avPlayer)
                pv.setVideoGravity(
                    when (playerState.resizeMode) {
                        ResizeMode.FIT -> AVLayerVideoGravityResizeAspect
                        ResizeMode.FILL -> AVLayerVideoGravityResizeAspectFill
                        ResizeMode.STRETCH -> AVLayerVideoGravityResize
                        ResizeMode.FIXED_16_9 -> AVLayerVideoGravityResizeAspect
                    }
                )
            }
        },
        modifier = modifier.fillMaxSize().background(Color.Black)
    )
}
