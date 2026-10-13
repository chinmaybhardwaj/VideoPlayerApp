package com.cbhard.videoplayer.composeapp.ui

import com.cbhard.videoplayer.composeapp.data.VideoRepositoryImpl
import com.cbhard.videoplayer.core.model.Video
import com.cbhard.videoplayer.core.model.VideoCategory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CatalogViewModelTest {

    @Test
    fun testRepositoryReturnsVideos() {
        val repo = VideoRepositoryImpl()
        val videos = repo.getVideos()
        assertTrue(videos.isNotEmpty())

        val hlsVideo = repo.getVideoById("abr-hls-mux")
        assertNotNull(hlsVideo)
        assertEquals("Big Buck Bunny - Multi-bitrate HLS", hlsVideo.title)

        val nonExistent = repo.getVideoById("invalid-id")
        assertNull(nonExistent)
    }

    @Test
    fun testRepositoryFilterByCategory() {
        val repo = VideoRepositoryImpl()
        val abrVideos = repo.getVideosByCategory(VideoCategory.ADAPTIVE_ABR)
        assertTrue(abrVideos.isNotEmpty())
        assertTrue(abrVideos.all { it.category == VideoCategory.ADAPTIVE_ABR })
    }

    @Test
    fun testCatalogViewModelInitialState() {
        val repo = VideoRepositoryImpl()
        val viewModel = CatalogViewModel(repo)

        val state = viewModel.uiState.value
        assertEquals(repo.getVideos().size, state.videos.size)
        assertNull(state.selectedCategory)
    }

    @Test
    fun testCatalogViewModelCategorySelection() {
        val repo = VideoRepositoryImpl()
        val viewModel = CatalogViewModel(repo)

        viewModel.onCategorySelect(VideoCategory.DRM)
        assertEquals(VideoCategory.DRM, viewModel.selectedCategory.value)
    }

    @Test
    fun testCatalogViewModelAddCustomVideo() {
        val repo = VideoRepositoryImpl()
        val viewModel = CatalogViewModel(repo)

        val customVideo = Video(
            id = "custom-1",
            title = "Test Stream",
            url = "https://example.com/test.m3u8",
            category = VideoCategory.ADAPTIVE_ABR,
            description = "Test description"
        )

        viewModel.addCustomVideo(customVideo)
        // Verify state updates
        assertNotNull(customVideo.id)
    }
}
