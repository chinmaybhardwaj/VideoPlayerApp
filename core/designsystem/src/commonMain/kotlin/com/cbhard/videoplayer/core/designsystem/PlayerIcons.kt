package com.cbhard.videoplayer.core.designsystem

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object PlayerIcons {
    val Back: ImageVector = ImageVector.Builder(
        name = "Back",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(20f, 11f)
            horizontalLineTo(7.83f)
            lineTo(13.42f, 5.41f)
            lineTo(12f, 4f)
            lineTo(4f, 12f)
            lineTo(12f, 20f)
            lineTo(13.41f, 18.59f)
            lineTo(7.83f, 13f)
            horizontalLineTo(20f)
            verticalLineTo(11f)
            close()
        }
    }.build()

    val Close: ImageVector = ImageVector.Builder(
        name = "Close",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(19f, 6.41f)
            lineTo(17.59f, 5f)
            lineTo(12f, 10.59f)
            lineTo(6.41f, 5f)
            lineTo(5f, 6.41f)
            lineTo(10.59f, 12f)
            lineTo(5f, 17.59f)
            lineTo(6.41f, 19f)
            lineTo(12f, 13.41f)
            lineTo(17.59f, 19f)
            lineTo(19f, 17.59f)
            lineTo(13.41f, 12f)
            lineTo(19f, 6.41f)
            close()
        }
    }.build()

    val Settings: ImageVector = ImageVector.Builder(
        name = "Settings",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(19.14f, 12.94f)
            curveTo(19.17f, 12.64f, 19.2f, 12.33f, 19.2f, 12f)
            curveTo(19.2f, 11.67f, 19.17f, 11.36f, 19.14f, 11.06f)
            lineTo(21.41f, 9.28f)
            curveTo(21.62f, 9.12f, 21.68f, 8.82f, 21.54f, 8.59f)
            lineTo(19.39f, 4.86f)
            curveTo(19.25f, 4.62f, 18.96f, 4.54f, 18.73f, 4.63f)
            lineTo(16.06f, 5.71f)
            curveTo(15.5f, 5.28f, 14.89f, 4.93f, 14.22f, 4.67f)
            lineTo(13.82f, 1.83f)
            curveTo(13.78f, 1.57f, 13.56f, 1.38f, 13.3f, 1.38f)
            horizontalLineTo(9f)
            curveTo(8.74f, 1.38f, 8.52f, 1.57f, 8.48f, 1.83f)
            lineTo(8.08f, 4.67f)
            curveTo(7.41f, 4.93f, 6.8f, 5.28f, 6.24f, 5.71f)
            lineTo(3.57f, 4.63f)
            curveTo(3.34f, 4.54f, 3.05f, 4.62f, 2.91f, 4.86f)
            lineTo(0.76f, 8.59f)
            curveTo(0.62f, 8.82f, 0.68f, 9.12f, 0.89f, 9.28f)
            lineTo(3.16f, 11.06f)
            curveTo(3.13f, 11.36f, 3.1f, 11.67f, 3.1f, 12f)
            curveTo(3.1f, 12.33f, 3.13f, 12.64f, 3.16f, 12.94f)
            lineTo(0.89f, 14.72f)
            curveTo(0.68f, 14.88f, 0.62f, 15.18f, 0.76f, 15.41f)
            lineTo(2.91f, 19.14f)
            curveTo(3.05f, 19.38f, 3.34f, 19.46f, 3.57f, 19.37f)
            lineTo(6.24f, 18.29f)
            curveTo(6.8f, 18.72f, 7.41f, 19.07f, 8.08f, 19.33f)
            lineTo(8.48f, 22.17f)
            curveTo(8.52f, 22.43f, 8.74f, 22.62f, 9f, 22.62f)
            horizontalLineTo(13.3f)
            curveTo(13.56f, 22.62f, 13.78f, 22.43f, 13.82f, 22.17f)
            lineTo(14.22f, 19.33f)
            curveTo(14.89f, 19.07f, 15.5f, 18.72f, 16.06f, 18.29f)
            lineTo(18.73f, 19.37f)
            curveTo(18.96f, 19.46f, 19.25f, 19.38f, 19.39f, 19.14f)
            lineTo(21.54f, 15.41f)
            curveTo(21.68f, 15.18f, 21.62f, 14.88f, 21.41f, 14.72f)
            lineTo(19.14f, 12.94f)
            close()
            moveTo(11.15f, 15.5f)
            curveTo(9.22f, 15.5f, 7.65f, 13.93f, 7.65f, 12f)
            curveTo(7.65f, 10.07f, 9.22f, 8.5f, 11.15f, 8.5f)
            curveTo(13.08f, 8.5f, 14.65f, 10.07f, 14.65f, 12f)
            curveTo(14.65f, 13.93f, 13.08f, 15.5f, 11.15f, 15.5f)
            close()
        }
    }.build()

    val Play: ImageVector = ImageVector.Builder(
        name = "Play",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(8f, 5f)
            verticalLineTo(19f)
            lineTo(19f, 12f)
            lineTo(8f, 5f)
            close()
        }
    }.build()

    val Info: ImageVector = ImageVector.Builder(
        name = "Info",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(12f, 2f)
            curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
            curveTo(2f, 17.52f, 6.48f, 22f, 12f, 22f)
            curveTo(17.52f, 22f, 22f, 17.52f, 22f, 12f)
            curveTo(22f, 6.48f, 17.52f, 2f, 12f, 2f)
            close()
            moveTo(13f, 17f)
            horizontalLineTo(11f)
            verticalLineTo(11f)
            horizontalLineTo(13f)
            verticalLineTo(17f)
            close()
            moveTo(13f, 9f)
            horizontalLineTo(11f)
            verticalLineTo(7f)
            horizontalLineTo(13f)
            verticalLineTo(9f)
            close()
        }
    }.build()

    val Check: ImageVector = ImageVector.Builder(
        name = "Check",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(9f, 16.17f)
            lineTo(4.83f, 12f)
            lineTo(3.41f, 13.41f)
            lineTo(9f, 19f)
            lineTo(21f, 7f)
            lineTo(19.59f, 5.59f)
            lineTo(9f, 16.17f)
            close()
        }
    }.build()

    val Lock: ImageVector = ImageVector.Builder(
        name = "Lock",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(18f, 8f)
            horizontalLineTo(17f)
            verticalLineTo(6f)
            curveTo(17f, 3.24f, 14.76f, 1f, 12f, 1f)
            curveTo(9.24f, 1f, 7f, 3.24f, 7f, 6f)
            verticalLineTo(8f)
            horizontalLineTo(6f)
            curveTo(4.9f, 8f, 4f, 8.9f, 4f, 10f)
            verticalLineTo(20f)
            curveTo(4f, 21.1f, 4.9f, 22f, 6f, 22f)
            horizontalLineTo(18f)
            curveTo(19.1f, 22f, 20f, 21.1f, 20f, 20f)
            verticalLineTo(10f)
            curveTo(20f, 8.9f, 19.1f, 8f, 18f, 8f)
            close()
            moveTo(9f, 6f)
            curveTo(9f, 4.34f, 10.34f, 3f, 12f, 3f)
            curveTo(13.66f, 3f, 15f, 4.34f, 15f, 6f)
            verticalLineTo(8f)
            horizontalLineTo(9f)
            verticalLineTo(6f)
            close()
            moveTo(18f, 20f)
            horizontalLineTo(6f)
            verticalLineTo(10f)
            horizontalLineTo(18f)
            verticalLineTo(20f)
            close()
        }
    }.build()

    val Replay: ImageVector = ImageVector.Builder(
        name = "Replay",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(17.65f, 6.35f)
            curveTo(16.2f, 4.9f, 14.21f, 4f, 12f, 4f)
            curveTo(7.58f, 4f, 4.01f, 7.58f, 4.01f, 12f)
            curveTo(4.01f, 16.42f, 7.58f, 20f, 12f, 20f)
            curveTo(15.73f, 20f, 18.84f, 17.45f, 19.73f, 14f)
            horizontalLineTo(17.65f)
            curveTo(16.83f, 16.33f, 14.61f, 18f, 12f, 18f)
            curveTo(8.69f, 18f, 6f, 15.31f, 6f, 12f)
            curveTo(6f, 8.69f, 8.69f, 6f, 12f, 6f)
            curveTo(13.66f, 6f, 15.14f, 6.69f, 16.22f, 7.78f)
            lineTo(13f, 11f)
            horizontalLineTo(20f)
            verticalLineTo(4f)
            lineTo(17.65f, 6.35f)
            close()
        }
    }.build()

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
