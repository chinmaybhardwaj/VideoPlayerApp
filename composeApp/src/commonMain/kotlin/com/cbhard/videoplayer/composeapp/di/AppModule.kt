package com.cbhard.videoplayer.composeapp.di

import com.cbhard.videoplayer.composeapp.data.VideoRepository
import com.cbhard.videoplayer.composeapp.data.VideoRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
    single<VideoRepository> { VideoRepositoryImpl() }
}
