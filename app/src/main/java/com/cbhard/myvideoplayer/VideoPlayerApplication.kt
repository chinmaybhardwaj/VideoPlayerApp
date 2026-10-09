package com.cbhard.myvideoplayer

import android.app.Application
import com.cbhard.videoplayer.composeapp.di.appModule
import com.cbhard.videoplayer.core.player.di.playerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class VideoPlayerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@VideoPlayerApplication)
            modules(appModule, playerModule)
        }
    }
}