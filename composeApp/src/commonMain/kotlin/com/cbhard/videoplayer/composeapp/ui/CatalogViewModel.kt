package com.cbhard.videoplayer.composeapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbhard.videoplayer.composeapp.data.VideoRepository
import com.cbhard.videoplayer.core.model.Video
import com.cbhard.videoplayer.core.model.VideoCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class CatalogUiState(
    val videos: List<Video> = emptyList(),
    val selectedCategory: VideoCategory? = null,
    val isLoading: Boolean = false
)

class CatalogViewModel(
    private val videoRepository: VideoRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<VideoCategory?>(null)
    val selectedCategory: StateFlow<VideoCategory?> = _selectedCategory.asStateFlow()

    private val _customVideos = MutableStateFlow<List<Video>>(emptyList())

    val uiState: StateFlow<CatalogUiState> = combine(
        videoRepository.getVideosStream(),
        _selectedCategory,
        _customVideos
    ) { repoVideos, category, customVideos ->
        val allVideos = customVideos + repoVideos
        val filtered = if (category == null) allVideos else allVideos.filter { it.category == category }
        CatalogUiState(
            videos = filtered,
            selectedCategory = category,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CatalogUiState(videos = videoRepository.getVideos())
    )

    fun onCategorySelect(category: VideoCategory?) {
        _selectedCategory.value = category
    }

    fun addCustomVideo(video: Video) {
        _customVideos.value = listOf(video) + _customVideos.value
    }
}
