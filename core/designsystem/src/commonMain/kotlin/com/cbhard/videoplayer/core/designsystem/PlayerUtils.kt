package com.cbhard.videoplayer.core.designsystem

import kotlin.math.max

fun formatDuration(ms: Long): String {
    val totalSeconds = max(0L, ms / 1000)
    val seconds = totalSeconds % 60
    val minutes = (totalSeconds / 60) % 60
    val hours = totalSeconds / 3600

    return if (hours > 0) {
        val hStr = hours.toString()
        val mStr = minutes.toString().padStart(2, '0')
        val sStr = seconds.toString().padStart(2, '0')
        "$hStr:$mStr:$sStr"
    } else {
        val mStr = minutes.toString().padStart(2, '0')
        val sStr = seconds.toString().padStart(2, '0')
        "$mStr:$sStr"
    }
}

fun formatBitrate(bps: Long): String {
    return when {
        bps >= 1_000_000 -> "${(bps / 100000) / 10.0} Mbps"
        bps >= 1_000 -> "${bps / 1000} kbps"
        bps > 0 -> "$bps bps"
        else -> "0 kbps"
    }
}
