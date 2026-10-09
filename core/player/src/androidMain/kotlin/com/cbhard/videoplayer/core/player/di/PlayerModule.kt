package com.cbhard.videoplayer.core.player.di

import com.cbhard.videoplayer.core.player.AndroidPlayerController
import com.cbhard.videoplayer.core.player.PlayerController
import org.koin.core.module.Module
import org.koin.dsl.module

val playerModule: Module = module {
    single<AndroidPlayerController> { AndroidPlayerController(get()) }
    single<PlayerController> { get<AndroidPlayerController>() }
}
