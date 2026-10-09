package com.cbhard.videoplayer.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PlayerDarkBackground = Color(0xFF0A0E17)
val PlayerSurface = Color(0xFF131A29)
val PlayerSurfaceVariant = Color(0xFF1F293D)
val PlayerPrimary = Color(0xFF3B82F6) // Electric blue
val PlayerSecondary = Color(0xFF10B981) // Emerald green accent
val PlayerTertiary = Color(0xFF8B5CF6) // Purple accent
val PlayerError = Color(0xFFEF4444)
val PlayerOnBackground = Color(0xFFF8FAFC)
val PlayerOnSurface = Color(0xFFF1F5F9)
val PlayerScrim = Color(0x99000000)

private val DarkColorScheme = darkColorScheme(
    primary = PlayerPrimary,
    secondary = PlayerSecondary,
    tertiary = PlayerTertiary,
    background = PlayerDarkBackground,
    surface = PlayerSurface,
    surfaceVariant = PlayerSurfaceVariant,
    onBackground = PlayerOnBackground,
    onSurface = PlayerOnSurface,
    error = PlayerError
)

@Composable
fun VideoPlayerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
