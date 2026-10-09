package com.cbhard.videoplayer.core.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cbhard.videoplayer.core.player.PlayerState

@Composable
fun StatsForNerdsOverlay(
    playerState: PlayerState,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val metrics = playerState.metrics
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .padding(16.dp)
            .clip(shape)
            .background(Color(0xCC0A0E17))
            .border(1.dp, Color(0x443B82F6), shape)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.width(320.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF3B82F6), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "STATS FOR NERDS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.padding(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Stats",
                        tint = Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Stats items
            StatRow(label = "Video ID", value = playerState.currentVideo?.id ?: "N/A")
            StatRow(label = "Resolution", value = metrics.resolution)
            StatRow(label = "Video Codec", value = metrics.videoCodec)
            StatRow(label = "Audio Codec", value = metrics.audioCodec)
            StatRow(label = "Current Bitrate", value = formatBitrate(metrics.currentBitrateBps))
            StatRow(label = "Est. Bandwidth", value = formatBitrate(metrics.estimatedBandwidthBps))
            StatRow(
                label = "Buffer Ahead",
                value = "${metrics.bufferDurationMs / 1000.0}s (${metrics.bufferPercentage}%)"
            )

            // Buffer health bar
            LinearProgressIndicator(
                progress = { (metrics.bufferDurationMs / 30000f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = if (metrics.bufferDurationMs > 5000) Color(0xFF10B981) else Color(0xFFEF4444),
                trackColor = Color(0x33FFFFFF)
            )

            StatRow(label = "Dropped Frames", value = "${metrics.droppedFrames}")
            StatRow(label = "Buffer Profile", value = playerState.bufferProfile.title)
            StatRow(label = "Playback Speed", value = "${playerState.playbackSpeed}x")
            StatRow(label = "Selected Quality", value = playerState.selectedQuality.name)
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF94A3B8),
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            fontFamily = FontFamily.Monospace
        )
    }
}
