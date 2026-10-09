package com.cbhard.videoplayer.core.designsystem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cbhard.videoplayer.core.model.BufferProfile
import com.cbhard.videoplayer.core.player.PlayerEvent
import com.cbhard.videoplayer.core.player.PlayerState

private enum class SettingsSection {
    MAIN,
    QUALITY,
    AUDIO,
    SUBTITLES,
    SPEED,
    BUFFER
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSettingsSheet(
    playerState: PlayerState,
    onEvent: (PlayerEvent) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var currentSection by remember { mutableStateOf(SettingsSection.MAIN) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF131A29),
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (currentSection) {
                        SettingsSection.MAIN -> "Playback Settings"
                        SettingsSection.QUALITY -> "Quality Selection"
                        SettingsSection.AUDIO -> "Audio Track"
                        SettingsSection.SUBTITLES -> "Subtitles & CC"
                        SettingsSection.SPEED -> "Playback Speed"
                        SettingsSection.BUFFER -> "Buffering Profile"
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                if (currentSection != SettingsSection.MAIN) {
                    Text(
                        text = "Back",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF3B82F6),
                        modifier = Modifier
                            .clickable { currentSection = SettingsSection.MAIN }
                            .padding(8.dp)
                    )
                } else {
                    IconButton(onClick = onDismiss) {
                        Icon(PlayerIcons.Close, contentDescription = "Close", tint = Color.LightGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (currentSection) {
                SettingsSection.MAIN -> {
                    SettingsMenuRow(
                        icon = PlayerIcons.Settings,
                        title = "Quality",
                        currentValue = playerState.selectedQuality.name,
                        onClick = { currentSection = SettingsSection.QUALITY }
                    )
                    SettingsMenuRow(
                        icon = PlayerIcons.Info,
                        title = "Audio",
                        currentValue = playerState.selectedAudioTrack?.label ?: "Default",
                        onClick = { currentSection = SettingsSection.AUDIO }
                    )
                    SettingsMenuRow(
                        icon = PlayerIcons.AspectRatio,
                        title = "Subtitles",
                        currentValue = playerState.selectedSubtitle.label,
                        onClick = { currentSection = SettingsSection.SUBTITLES }
                    )
                    SettingsMenuRow(
                        icon = PlayerIcons.Play,
                        title = "Playback Speed",
                        currentValue = "${playerState.playbackSpeed}x",
                        onClick = { currentSection = SettingsSection.SPEED }
                    )
                    SettingsMenuRow(
                        icon = PlayerIcons.Replay,
                        title = "Buffering Strategy",
                        currentValue = playerState.bufferProfile.title,
                        onClick = { currentSection = SettingsSection.BUFFER }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Anti-Screen Recording (FLAG_SECURE) Switch Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                PlayerIcons.Security,
                                contentDescription = null,
                                tint = if (playerState.isFlagSecureEnabled) Color(0xFF10B981) else Color(0xFF94A3B8),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Anti-Screen Recording (FLAG_SECURE)",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                                Text(
                                    text = if (playerState.isFlagSecureEnabled)
                                        "Active: Screen capture & screenshots blocked"
                                    else
                                        "Disabled: Recording permitted",
                                    fontSize = 12.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }

                        Switch(
                            checked = playerState.isFlagSecureEnabled,
                            onCheckedChange = { onEvent(PlayerEvent.SetFlagSecure(it)) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF10B981)
                            )
                        )
                    }
                }

                SettingsSection.QUALITY -> {
                    LazyColumn {
                        items(playerState.availableQualities) { quality ->
                            SelectableItemRow(
                                title = quality.name,
                                subtitle = if (quality.codec != null) "Codec: ${quality.codec}" else null,
                                isSelected = quality.id == playerState.selectedQuality.id,
                                onClick = {
                                    onEvent(PlayerEvent.SelectQuality(quality))
                                    currentSection = SettingsSection.MAIN
                                }
                            )
                        }
                    }
                }

                SettingsSection.AUDIO -> {
                    if (playerState.availableAudioTracks.isEmpty()) {
                        Text(
                            text = "No alternate audio tracks available in this stream.",
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyColumn {
                            items(playerState.availableAudioTracks) { track ->
                                SelectableItemRow(
                                    title = track.label,
                                    subtitle = "Language: ${track.language}",
                                    isSelected = track.id == playerState.selectedAudioTrack?.id,
                                    onClick = {
                                        onEvent(PlayerEvent.SelectAudioTrack(track))
                                        currentSection = SettingsSection.MAIN
                                    }
                                )
                            }
                        }
                    }
                }

                SettingsSection.SUBTITLES -> {
                    LazyColumn {
                        items(playerState.availableSubtitles) { subtitle ->
                            SelectableItemRow(
                                title = subtitle.label,
                                subtitle = if (subtitle.language.isNotEmpty()) "Language: ${subtitle.language}" else null,
                                isSelected = subtitle.id == playerState.selectedSubtitle.id,
                                onClick = {
                                    onEvent(PlayerEvent.SelectSubtitle(subtitle))
                                    currentSection = SettingsSection.MAIN
                                }
                            )
                        }
                    }
                }

                SettingsSection.SPEED -> {
                    val speeds = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f)
                    LazyColumn {
                        items(speeds) { speed ->
                            SelectableItemRow(
                                title = if (speed == 1.0f) "1.0x (Normal)" else "${speed}x",
                                isSelected = playerState.playbackSpeed == speed,
                                onClick = {
                                    onEvent(PlayerEvent.SetPlaybackSpeed(speed))
                                    currentSection = SettingsSection.MAIN
                                }
                            )
                        }
                    }
                }

                SettingsSection.BUFFER -> {
                    LazyColumn {
                        items(BufferProfile.entries) { profile ->
                            SelectableItemRow(
                                title = profile.title,
                                subtitle = profile.subtitle,
                                isSelected = playerState.bufferProfile == profile,
                                onClick = {
                                    onEvent(PlayerEvent.SetBufferProfile(profile))
                                    currentSection = SettingsSection.MAIN
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SettingsMenuRow(
    icon: ImageVector,
    title: String,
    currentValue: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.White)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = currentValue, fontSize = 14.sp, color = Color(0xFF64748B))
            Spacer(modifier = Modifier.width(8.dp))
            Icon(PlayerIcons.ChevronRight, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun SelectableItemRow(
    title: String,
    subtitle: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color(0xFF3B82F6) else Color.White
            )
            if (subtitle != null) {
                Text(text = subtitle, fontSize = 12.sp, color = Color(0xFF94A3B8))
            }
        }
        if (isSelected) {
            Icon(PlayerIcons.Check, contentDescription = "Selected", tint = Color(0xFF3B82F6))
        }
    }
}
