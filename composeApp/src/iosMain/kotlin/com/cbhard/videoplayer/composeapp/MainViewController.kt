package com.cbhard.videoplayer.composeapp

import androidx.compose.ui.window.ComposeUIViewController
import com.cbhard.videoplayer.composeapp.di.appModule
import com.cbhard.videoplayer.core.player.di.playerModule
import org.koin.core.context.startKoin

private var isKoinStarted = false

fun initKoin() {
    if (!isKoinStarted) {
        startKoin {
            modules(appModule, playerModule)
        }
        isKoinStarted = true
    }
}

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}
