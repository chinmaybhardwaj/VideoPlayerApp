package com.cbhard.videoplayer.composeapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cbhard.videoplayer.core.designsystem.PlayerIcons
import com.cbhard.videoplayer.core.model.Video
import com.cbhard.videoplayer.core.model.VideoCategory
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CatalogScreen(
    onVideoSelected: (Video) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CatalogViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CatalogScreen(
        videos = uiState.videos,
        selectedCategory = uiState.selectedCategory,
        onCategorySelect = { viewModel.onCategorySelect(it) },
        onCustomVideoAdd = { viewModel.addCustomVideo(it) },
        onVideoSelected = onVideoSelected,
        modifier = modifier
    )
}

@Composable
fun CatalogScreen(
    videos: List<Video>,
    onVideoSelected: (Video) -> Unit,
    modifier: Modifier = Modifier,
    selectedCategory: VideoCategory? = null,
    onCategorySelect: ((VideoCategory?) -> Unit)? = null,
    onCustomVideoAdd: ((Video) -> Unit)? = null
) {
    var internalSelectedCategory by remember { mutableStateOf<VideoCategory?>(null) }
    val effectiveCategory = if (onCategorySelect != null) selectedCategory else internalSelectedCategory
    var showCustomUrlDialog by remember { mutableStateOf(false) }
    var showDrmGuideDialog by remember { mutableStateOf(false) }

    val filteredVideos = remember(effectiveCategory, videos) {
        if (effectiveCategory == null) videos else videos.filter { it.category == effectiveCategory }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFF0A0E17),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCustomUrlDialog = true },
                containerColor = Color(0xFF3B82F6),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(PlayerIcons.Play, contentDescription = "Play URL")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // App Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CMP Video Player",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Compose Multiplatform • Media3 • Codecs & ABR",
                            fontSize = 13.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    // DRM & Security Guide Icon
                    IconButton(onClick = { showDrmGuideDialog = true }) {
                        Icon(
                            PlayerIcons.Security,
                            contentDescription = "DRM & Security Guide",
                            tint = Color(0xFF10B981)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Category Chips Scroll
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CategoryChip(
                        label = "All Streams",
                        isSelected = effectiveCategory == null,
                        onClick = {
                            if (onCategorySelect != null) onCategorySelect(null) else internalSelectedCategory = null
                        }
                    )
                    VideoCategory.entries.forEach { category ->
                        CategoryChip(
                            label = category.displayName.substringBefore(" (").substringBefore(" Showcase"),
                            isSelected = effectiveCategory == category,
                            onClick = {
                                if (onCategorySelect != null) onCategorySelect(category) else internalSelectedCategory = category
                            }
                        )
                    }
                }
            }

            // Video Cards List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredVideos) { video ->
                    VideoCard(
                        video = video,
                        onClick = { onVideoSelected(video) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }

    // Custom URL Input Dialog
    if (showCustomUrlDialog) {
        CustomUrlDialog(
            onDismiss = { showCustomUrlDialog = false },
            onPlay = { customVideo ->
                showCustomUrlDialog = false
                onCustomVideoAdd?.invoke(customVideo)
                onVideoSelected(customVideo)
            }
        )
    }

    // Educational DRM & Security Guide Dialog
    if (showDrmGuideDialog) {
        DrmGuideDialog(onDismiss = { showDrmGuideDialog = false })
    }
}

@Composable
private fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Color(0xFF3B82F6) else Color(0xFF1E293B))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF94A3B8)
        )
    }
}

@Composable
private fun VideoCard(
    video: Video,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131A29)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x333B82F6))
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = video.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = video.category.displayName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = when (video.category) {
                            VideoCategory.ADAPTIVE_ABR -> Color(0xFF60A5FA)
                            VideoCategory.CODECS -> Color(0xFFA78BFA)
                            VideoCategory.MULTI_TRACK -> Color(0xFF34D399)
                            VideoCategory.DRM -> Color(0xFFFBBF24)
                        }
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3B82F6)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        PlayerIcons.Play,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = video.description,
                fontSize = 13.sp,
                color = Color(0xFF94A3B8),
                lineHeight = 18.sp
            )

            video.codecInfo?.let { codec ->
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .background(Color(0xFF1E293B), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = codec,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFFCBD5E1)
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomUrlDialog(
    onDismiss: () -> Unit,
    onPlay: (Video) -> Unit
) {
    var urlText by remember { mutableStateOf("") }
    var titleText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF131A29),
        title = {
            Text("Play Custom Stream URL", color = Color.White, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Enter any HLS (.m3u8), DASH (.mpd), or progressive MP4 video URL:",
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8)
                )
                OutlinedTextField(
                    value = titleText,
                    onValueChange = { titleText = it },
                    label = { Text("Stream Title") },
                    placeholder = { Text("My Test Stream") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                OutlinedTextField(
                    value = urlText,
                    onValueChange = { urlText = it },
                    label = { Text("Stream URL") },
                    placeholder = { Text("https://example.com/stream.m3u8") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (urlText.isNotBlank()) {
                        val video = Video(
                            id = "custom-${urlText.hashCode()}",
                            title = if (titleText.isNotBlank()) titleText else "Custom Stream",
                            url = urlText.trim(),
                            category = VideoCategory.ADAPTIVE_ABR,
                            description = "User provided custom video stream.",
                            codecInfo = if (urlText.contains(".m3u8")) "Custom HLS" else if (urlText.contains(".mpd")) "Custom DASH" else "Custom MP4"
                        )
                        onPlay(video)
                    }
                },
                enabled = urlText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
            ) {
                Text("Play Stream")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.LightGray)
            }
        }
    )
}

@Composable
private fun DrmGuideDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF131A29),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(PlayerIcons.Security, contentDescription = null, tint = Color(0xFF10B981))
                Spacer(modifier = Modifier.width(8.dp))
                Text("DRM & Anti-Recording Guide", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "How OTT Platforms (Netflix/Prime) Protect Playback:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38BDF8)
                )
                Text(
                    text = "1. Operating System Level (FLAG_SECURE on Android):\n" +
                            "Setting WindowManager.LayoutParams.FLAG_SECURE forces the Android WindowManager to treat the surface as secure. Any OS screen recorder, screenshot, or HDMI mirror without HDCP produces a pure black frame.",
                    fontSize = 12.sp,
                    color = Color(0xFFCBD5E1),
                    lineHeight = 17.sp
                )
                Text(
                    text = "2. iOS Screen Capture Detection:\n" +
                            "iOS provides UIScreen.isCaptured and UIScreen.capturedDidChangeNotification. When screen recording is activated, the player layer is blanked out and muted to prevent piracy.",
                    fontSize = 12.sp,
                    color = Color(0xFFCBD5E1),
                    lineHeight = 17.sp
                )
                Text(
                    text = "3. Hardware DRM (Widevine L1 vs L3 & FairPlay):\n" +
                            "Widevine L1 routes encrypted audio/video payloads into the SoC's Trusted Execution Environment (TEE). The decrypted frames render straight to hardware overlays, making CPU frame capture mathematically impossible.",
                    fontSize = 12.sp,
                    color = Color(0xFFCBD5E1),
                    lineHeight = 17.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
            ) {
                Text("Got It")
            }
        }
    )
}
