package com.cbhard.videoplayer.core.player.di

import com.cbhard.videoplayer.core.player.IosPlayerController
import com.cbhard.videoplayer.core.player.PlayerController
import org.koin.core.module.Module
import org.koin.dsl.module

val playerModule: Module = module {
    single<IosPlayerController> { IosPlayerController() }
    single<PlayerController> { get<IosPlayerController>() }
}
