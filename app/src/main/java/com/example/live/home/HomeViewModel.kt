package com.example.live.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.live.data.repository.HomeContentPostsRepository
import com.example.live.data.model.Post
import com.example.live.search.DataLoadingUiState
import com.example.live.ui.pullrefresh.PullToRefreshLayoutState
import com.example.live.ui.pullrefresh.RefreshIndicatorState
import com.example.live.util.DateUtils
import com.example.live.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeContentPostsRepository,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {

    var homeUiState by mutableStateOf(
        HomeUiState(
            pullToRefreshState = PullToRefreshLayoutState(
                onTimeUpdated = { timeElapsed ->
                    DateUtils.getTimePassedInHourMinSec(resourceProvider, timeElapsed)
                })
        )
    )
        private set

    private var currentPage = 1

    init {
        loadPosts(DataLoadingUiState.LoadingType.INITIAL_LOAD)
    }

    fun loadPosts(loadingType: DataLoadingUiState.LoadingType) {
        homeUiState = homeUiState.copy(loadingType = loadingType)
        currentPage =
            if (loadingType == DataLoadingUiState.LoadingType.INITIAL_LOAD || loadingType == DataLoadingUiState.LoadingType.PULL_REFRESH)
                1
            else
                currentPage.inc()
        viewModelScope.launch {
            homeUiState = homeUiState.copy(
                isLoading = true,
                dataLoadingUiState = DataLoadingUiState.Loading
            )
            try {
                delay(2000)
                repository.savePosts(page = currentPage)
                val newPosts = repository.posts.first()
                homeUiState.posts.clear()
                homeUiState.posts.addAll(newPosts)
                if (loadingType == DataLoadingUiState.LoadingType.PULL_REFRESH) {
                    homeUiState.pullToRefreshState.updateRefreshState(RefreshIndicatorState.Default)
                }
                homeUiState = homeUiState.copy(dataLoadingUiState = DataLoadingUiState.Success)
            } catch (e: IOException) {
                homeUiState.pullToRefreshState.updateRefreshState(RefreshIndicatorState.Default)
                homeUiState = homeUiState.copy(dataLoadingUiState = DataLoadingUiState.Error)
            } catch (e: HttpException) {
                homeUiState.pullToRefreshState.updateRefreshState(RefreshIndicatorState.Default)
                homeUiState = homeUiState.copy(dataLoadingUiState = DataLoadingUiState.Error)
            } finally {
                homeUiState = homeUiState.copy(isLoading = false)
            }
        }
    }
}

data class HomeUiState(
    val dataLoadingUiState: DataLoadingUiState = DataLoadingUiState.Loading,
    val posts: MutableList<Post> = mutableListOf(),
    val isLoading: Boolean = false,
    val loadingType: DataLoadingUiState.LoadingType = DataLoadingUiState.LoadingType.INITIAL_LOAD,
    val pullToRefreshState: PullToRefreshLayoutState
)