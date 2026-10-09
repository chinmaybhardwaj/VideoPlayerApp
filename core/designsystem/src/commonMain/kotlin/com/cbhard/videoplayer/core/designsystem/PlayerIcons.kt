package com.cbhard.videoplayer.core.designsystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object PlayerIcons {
    val Back = Icons.Default.ArrowBack
    val Close = Icons.Default.Close
    val Settings = Icons.Default.Settings
    val Play = Icons.Default.PlayArrow
    val Info = Icons.Default.Info
    val Check = Icons.Default.Check
    val Lock = Icons.Default.Lock
    val Replay = Icons.Default.Refresh

    val Pause: ImageVector = ImageVector.Builder(
        name = "Pause",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(6f, 19f)
            horizontalLineTo(10f)
            verticalLineTo(5f)
            horizontalLineTo(6f)
            verticalLineTo(19f)
            close()
            moveTo(14f, 5f)
            verticalLineTo(19f)
            horizontalLineTo(18f)
            verticalLineTo(5f)
            horizontalLineTo(14f)
            close()
        }
    }.build()

    val Rewind10: ImageVector = ImageVector.Builder(
        name = "Rewind10",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(11.99f, 5f)
            verticalLineTo(1f)
            lineTo(7f, 6f)
            lineTo(11.99f, 11f)
            verticalLineTo(7f)
            curveTo(15.31f, 7f, 18f, 9.69f, 18f, 13f)
            curveTo(18f, 16.31f, 15.31f, 19f, 11.99f, 19f)
            curveTo(8.68f, 19f, 6f, 16.31f, 6f, 13f)
            horizontalLineTo(4f)
            curveTo(4f, 17.42f, 7.58f, 21f, 11.99f, 21f)
            curveTo(16.41f, 21f, 20f, 17.42f, 20f, 13f)
            curveTo(20f, 8.58f, 16.41f, 5f, 11.99f, 5f)
            close()
        }
    }.build()

    val Forward10: ImageVector = ImageVector.Builder(
        name = "Forward10",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(12.01f, 5f)
            verticalLineTo(1f)
            lineTo(17f, 6f)
            lineTo(12.01f, 11f)
            verticalLineTo(7f)
            curveTo(8.69f, 7f, 6f, 9.69f, 6f, 13f)
            curveTo(6f, 16.31f, 8.69f, 19f, 12.01f, 19f)
            curveTo(15.32f, 19f, 18f, 16.31f, 18f, 13f)
            horizontalLineTo(20f)
            curveTo(20f, 17.42f, 16.42f, 21f, 12.01f, 21f)
            curveTo(7.59f, 21f, 4f, 17.42f, 4f, 13f)
            curveTo(4f, 8.58f, 7.59f, 5f, 12.01f, 5f)
            close()
        }
    }.build()

    val AspectRatio: ImageVector = ImageVector.Builder(
        name = "AspectRatio",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(19f, 12f)
            horizontalLineTo(17f)
            verticalLineTo(15f)
            horizontalLineTo(14f)
            verticalLineTo(17f)
            horizontalLineTo(19f)
            verticalLineTo(12f)
            close()
            moveTo(21f, 3f)
            horizontalLineTo(3f)
            curveTo(1.9f, 3f, 1f, 3.9f, 1f, 5f)
            verticalLineTo(19f)
            curveTo(1f, 20.1f, 1.9f, 21f, 3f, 21f)
            horizontalLineTo(21f)
            curveTo(22.1f, 21f, 23f, 20.1f, 23f, 19f)
            verticalLineTo(5f)
            curveTo(23f, 3.9f, 22.1f, 3f, 21f, 3f)
            close()
            moveTo(21f, 19f)
            horizontalLineTo(3f)
            verticalLineTo(5f)
            horizontalLineTo(21f)
            verticalLineTo(19f)
            close()
            moveTo(5f, 7f)
            horizontalLineTo(10f)
            verticalLineTo(9f)
            horizontalLineTo(7f)
            verticalLineTo(12f)
            horizontalLineTo(5f)
            verticalLineTo(7f)
            close()
        }
    }.build()

    val PiP: ImageVector = ImageVector.Builder(
        name = "PiP",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(19f, 7f)
            horizontalLineTo(11f)
            verticalLineTo(13f)
            horizontalLineTo(19f)
            verticalLineTo(7f)
            close()
            moveTo(21f, 3f)
            horizontalLineTo(3f)
            curveTo(1.9f, 3f, 1f, 3.9f, 1f, 5f)
            verticalLineTo(19f)
            curveTo(1f, 20.1f, 1.9f, 21f, 3f, 21f)
            horizontalLineTo(21f)
            curveTo(22.1f, 21f, 23f, 20.1f, 23f, 19f)
            verticalLineTo(5f)
            curveTo(23f, 3.9f, 22.1f, 3f, 21f, 3f)
            close()
            moveTo(21f, 19f)
            horizontalLineTo(3f)
            verticalLineTo(4.98f)
            horizontalLineTo(21f)
            verticalLineTo(19f)
            close()
        }
    }.build()

    val ChevronRight: ImageVector = ImageVector.Builder(
        name = "ChevronRight",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(10f, 6f)
            lineTo(8.59f, 7.41f)
            lineTo(13.17f, 12f)
            lineTo(8.59f, 16.59f)
            lineTo(10f, 18f)
            lineTo(16f, 12f)
            close()
        }
    }.build()

    val Security: ImageVector = ImageVector.Builder(
        name = "Security",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(12f, 1f)
            lineTo(3f, 5f)
            verticalLineTo(11f)
            curveTo(3f, 16.55f, 6.84f, 21.74f, 12f, 23f)
            curveTo(17.16f, 21.74f, 21f, 16.55f, 21f, 11f)
            verticalLineTo(5f)
            lineTo(12f, 1f)
            close()
        }
    }.build()
}
