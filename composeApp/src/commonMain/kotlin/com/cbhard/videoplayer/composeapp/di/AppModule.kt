package com.cbhard.videoplayer.composeapp.di

import com.cbhard.videoplayer.composeapp.data.VideoRepository
import com.cbhard.videoplayer.composeapp.data.VideoRepositoryImpl
import com.cbhard.videoplayer.composeapp.ui.CatalogViewModel
import com.cbhard.videoplayer.composeapp.ui.PlayerViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule: Module = module {
    single<VideoRepository> { VideoRepositoryImpl() }
    viewModel { CatalogViewModel(get()) }
    viewModel { PlayerViewModel(get()) }
}
