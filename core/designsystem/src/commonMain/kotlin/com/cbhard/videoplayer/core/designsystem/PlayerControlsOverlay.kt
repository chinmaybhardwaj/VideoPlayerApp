package com.cbhard.videoplayer.core.designsystem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cbhard.videoplayer.core.model.ResizeMode
import com.cbhard.videoplayer.core.player.PlayerEvent
import com.cbhard.videoplayer.core.player.PlayerState
import kotlinx.coroutines.delay

@Composable
fun PlayerControlsOverlay(
    playerState: PlayerState,
    onEvent: (PlayerEvent) -> Unit,
    onBackClick: () -> Unit,
    onPictureInPictureClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var controlsVisible by remember { mutableStateOf(true) }
    var isLocked by remember { mutableStateOf(false) }
    var isSettingsOpen by remember { mutableStateOf(false) }
    var seekNotice by remember { mutableStateOf<String?>(null) }
    var isScrubbing by remember { mutableStateOf(false) }
    var scrubPositionMs by remember { mutableLongStateOf(0L) }

    // Auto-hide controls after 4 seconds of inactivity
    LaunchedEffect(controlsVisible, playerState.isPlaying, isScrubbing) {
        if (controlsVisible && playerState.isPlaying && !isScrubbing) {
            delay(4000)
            controlsVisible = false
        }
    }

    LaunchedEffect(seekNotice) {
        if (seekNotice != null) {
            delay(800)
            seekNotice = null
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(isLocked) {
                detectTapGestures(
                    onTap = {
                        controlsVisible = !controlsVisible
                    },
                    onDoubleTap = { offset ->
                        if (!isLocked) {
                            val width = size.width
                            if (offset.x < width / 3f) {
                                onEvent(PlayerEvent.SeekBy(-10000L))
                                seekNotice = "-10s"
                            } else if (offset.x > width * 2f / 3f) {
                                onEvent(PlayerEvent.SeekBy(10000L))
                                seekNotice = "+10s"
                            } else {
                                onEvent(PlayerEvent.TogglePlayPause)
                            }
                        }
                    }
                )
            }
    ) {
        // Seek Ripple Banner
        if (seekNotice != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color(0x99000000), CircleShape)
                    .padding(horizontal = 24.dp, vertical = 14.dp)
            ) {
                Text(
                    text = seekNotice ?: "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Buffer Loading Spinner
        if (playerState.isLoading) {
            Box(
                modifier = Modifier.align(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF3B82F6),
                    modifier = Modifier.size(52.dp),
                    strokeWidth = 4.dp
                )
            }
        }

        // Animated Overlays (Top Bar, Center, Bottom Bar)
        AnimatedVisibility(
            visible = controlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xB3000000),
                                Color(0x33000000),
                                Color(0xB3000000)
                            )
                        )
                    )
            ) {
                // TOP BAR
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(PlayerIcons.Back, contentDescription = "Back", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(
                            modifier = Modifier.weight(1f, fill = false),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = playerState.currentVideo?.title ?: "Video Player",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            playerState.currentVideo?.codecInfo?.let { codec ->
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = codec,
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        // Lock screen button
                        IconButton(
                            onClick = { isLocked = !isLocked },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = PlayerIcons.Lock,
                                contentDescription = "Lock",
                                tint = if (isLocked) Color(0xFFEF4444) else Color.White
                            )
                        }

                        if (!isLocked) {
                            // "Stats for Nerds" Toggle
                            IconButton(
                                onClick = { onEvent(PlayerEvent.ToggleStatsOverlay) },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = PlayerIcons.Info,
                                    contentDescription = "Stats for Nerds",
                                    tint = if (playerState.isStatsOverlayVisible) Color(0xFF3B82F6) else Color.White
                                )
                            }

                            // PiP trigger
                            IconButton(
                                onClick = onPictureInPictureClick,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = PlayerIcons.PiP,
                                    contentDescription = "Picture in Picture",
                                    tint = Color.White
                                )
                            }

                            // Settings Bottom Sheet
                            IconButton(
                                onClick = { isSettingsOpen = true },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = PlayerIcons.Settings,
                                    contentDescription = "Settings",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                // CENTER CONTROLS (Only visible if not locked)
                if (!isLocked && !playerState.isLoading) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(bottom = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(36.dp)
                    ) {
                        // Rewind 10s
                        IconButton(
                            onClick = {
                                onEvent(PlayerEvent.SeekBy(-10000L))
                                seekNotice = "-10s"
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                PlayerIcons.Rewind10,
                                contentDescription = "Rewind 10s",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        // Play/Pause/Replay
                        Box(
                            modifier = Modifier
                                .size(68.dp)
                                .clip(CircleShape)
                                .background(Color(0x663B82F6))
                                .clickable { onEvent(PlayerEvent.TogglePlayPause) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (playerState.isPlaying) PlayerIcons.Pause else PlayerIcons.Play,
                                contentDescription = if (playerState.isPlaying) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(42.dp)
                            )
                        }

                        // Fast Forward 10s
                        IconButton(
                            onClick = {
                                onEvent(PlayerEvent.SeekBy(10000L))
                                seekNotice = "+10s"
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                PlayerIcons.Forward10,
                                contentDescription = "Forward 10s",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }

                // BOTTOM CONTROLS BAR (Only visible if not locked)
                if (!isLocked) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        // Scrubber Slider
                        val displayPos = if (isScrubbing) scrubPositionMs else playerState.currentPositionMs
                        val dur = playerState.durationMs

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = formatDuration(displayPos),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )

                            Slider(
                                value = if (dur > 0) displayPos.toFloat() / dur.toFloat() else 0f,
                                onValueChange = { fraction ->
                                    isScrubbing = true
                                    scrubPositionMs = (fraction * dur).toLong()
                                },
                                onValueChangeFinished = {
                                    isScrubbing = false
                                    onEvent(PlayerEvent.SeekTo(scrubPositionMs))
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 12.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF3B82F6),
                                    activeTrackColor = Color(0xFF3B82F6),
                                    inactiveTrackColor = Color(0x44FFFFFF)
                                )
                            )

                            Text(
                                text = formatDuration(dur),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF94A3B8)
                            )
                        }

                        // Bottom Actions: Aspect Ratio Mode & Track summary
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Active tracks badges
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0x333B82F6), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = playerState.selectedQuality.name,
                                        fontSize = 11.sp,
                                        color = Color(0xFF60A5FA)
                                    )
                                }

                                if (playerState.selectedSubtitle.id != "off") {
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0x3310B981), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "CC: ${playerState.selectedSubtitle.label}",
                                            fontSize = 11.sp,
                                            color = Color(0xFF34D399)
                                        )
                                    }
                                }
                            }

                            // Cycle Resize Mode Button
                            IconButton(onClick = {
                                val nextMode = when (playerState.resizeMode) {
                                    ResizeMode.FIT -> ResizeMode.FILL
                                    ResizeMode.FILL -> ResizeMode.FIXED_16_9
                                    ResizeMode.FIXED_16_9 -> ResizeMode.STRETCH
                                    ResizeMode.STRETCH -> ResizeMode.FIT
                                }
                                onEvent(PlayerEvent.SetResizeMode(nextMode))
                            }) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        PlayerIcons.AspectRatio,
                                        contentDescription = "Aspect Ratio",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = playerState.resizeMode.label.substringBefore(" "),
                                        fontSize = 11.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Stats for Nerds HUD overlay
        if (playerState.isStatsOverlayVisible) {
            StatsForNerdsOverlay(
                playerState = playerState,
                onClose = { onEvent(PlayerEvent.ToggleStatsOverlay) },
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Settings Sheet
        if (isSettingsOpen) {
            PlayerSettingsSheet(
                playerState = playerState,
                onEvent = onEvent,
                onDismiss = { isSettingsOpen = false }
            )
        }
    }
}
