package com.cbhard.myvideoplayer

import android.app.PictureInPictureParams
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.cbhard.videoplayer.composeapp.App
import com.cbhard.videoplayer.core.player.PlayerController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val playerController: PlayerController by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Observe playerState for dynamic FLAG_SECURE window management
        lifecycleScope.launch {
            playerController.playerState.collectLatest { state ->
                if (state.isFlagSecureEnabled) {
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_SECURE,
                        WindowManager.LayoutParams.FLAG_SECURE
                    )
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                }
            }
        }

        setContent {
            App(
                onPictureInPictureClick = { triggerPictureInPicture() },
                playerController = playerController,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    private fun triggerPictureInPicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val params = PictureInPictureParams.Builder()
                .setAspectRatio(Rational(16, 9))
                .build()
            enterPictureInPictureMode(params)
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        // Automatically enter PiP when home is pressed while video is playing
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && playerController.playerState.value.isPlaying) {
            triggerPictureInPicture()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            playerController.release()
        }
    }
}