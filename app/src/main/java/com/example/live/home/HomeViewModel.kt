package com.example.live.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.live.data.repository.LiveRepository
import com.example.live.database.model.Post
import com.example.live.search.SearchUiState
import com.example.live.ui.pullrefresh.PullToRefreshLayoutState
import com.example.live.ui.pullrefresh.RefreshIndicatorState
import com.example.live.util.DateUtils
import com.example.live.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: LiveRepository,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {

    private val _searchUiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val searchUiState: StateFlow<SearchUiState> = _searchUiState

    var posts = mutableListOf<Post>()
        private set
    private var currentPage = 1
    var isLoading by mutableStateOf(false)
        private set
    var loadingType: SearchUiState.LoadingType by mutableStateOf(SearchUiState.LoadingType.INITIAL_LOAD)
        private set

    val pullToRefreshState = PullToRefreshLayoutState(
        onTimeUpdated = { timeElapsed ->
            convertElapsedTimeIntoText(timeElapsed)
        }
    )

    init {
        loadPhotos(SearchUiState.LoadingType.INITIAL_LOAD)
    }

    fun loadPhotos(loadingType: SearchUiState.LoadingType) {
        this.loadingType = loadingType
        currentPage =
            if (loadingType == SearchUiState.LoadingType.INITIAL_LOAD || loadingType == SearchUiState.LoadingType.PULL_REFRESH)
                1
            else
                currentPage.inc()
        viewModelScope.launch {
            isLoading = true
            _searchUiState.value = SearchUiState.Loading
            _searchUiState.value = try {
                delay(2000)
                val newPhotos = repository.getPhotosFeed(page = currentPage)
                if (posts.size > 0
                    && (loadingType == SearchUiState.LoadingType.INITIAL_LOAD || loadingType == SearchUiState.LoadingType.PULL_REFRESH)
                )
                    posts.clear()
                posts.addAll(newPhotos)
                if (loadingType == SearchUiState.LoadingType.PULL_REFRESH) {
                    pullToRefreshState.updateRefreshState(RefreshIndicatorState.Default)
                }
                SearchUiState.Success(posts)
            } catch (e: IOException) {
                pullToRefreshState.updateRefreshState(RefreshIndicatorState.Default)
                SearchUiState.Error
            } catch (e: HttpException) {
                pullToRefreshState.updateRefreshState(RefreshIndicatorState.Default)
                SearchUiState.Error
            } finally {
                isLoading = false
            }
        }
    }

    private fun convertElapsedTimeIntoText(timeElapsed: Long): String {
        return DateUtils.getTimePassedInHourMinSec(resourceProvider, timeElapsed)
    }
}