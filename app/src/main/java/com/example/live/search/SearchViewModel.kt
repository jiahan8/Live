package com.example.live.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.live.data.repository.SearchContentPhotosRepository
import com.example.live.database.model.Post
import com.example.live.ui.pullrefresh.PullToRefreshLayoutState
import com.example.live.ui.pullrefresh.RefreshIndicatorState
import com.example.live.util.DateUtils
import com.example.live.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchContentPhotosRepository,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {

    var searchUiState by mutableStateOf(
        SearchUiState(
            pullToRefreshState = PullToRefreshLayoutState(
                onTimeUpdated = { timeElapsed ->
                    convertElapsedTimeIntoText(timeElapsed)
                })
        )
    )
        private set

    private var currentPage = 1
    private var searchPhotosJob: Job? = null
    private var loadPhotosJob: Job? = null

    init {
        loadPhotos(DataLoadingUiState.LoadingType.INITIAL_LOAD)
    }

    fun loadPhotos(loadingType: DataLoadingUiState.LoadingType) {
        searchUiState = searchUiState.copy(loadingType = loadingType)
        currentPage =
            if (loadingType == DataLoadingUiState.LoadingType.INITIAL_LOAD || loadingType == DataLoadingUiState.LoadingType.PULL_REFRESH)
                1
            else
                currentPage.inc()
        loadPhotosJob?.cancel()
        searchPhotosJob?.cancel()
        loadPhotosJob = viewModelScope.launch {
            searchUiState = searchUiState.copy(
                isLoading = true,
                dataLoadingUiState = DataLoadingUiState.Loading
            )
            try {
                delay(2000)
                val newPhotos = repository.getPhotos(page = currentPage)
                if (searchUiState.photos.isNotEmpty()
                    && (loadingType == DataLoadingUiState.LoadingType.INITIAL_LOAD || loadingType == DataLoadingUiState.LoadingType.PULL_REFRESH)
                )
                    searchUiState.photos.clear()
                searchUiState.photos.addAll(newPhotos)
                if (loadingType == DataLoadingUiState.LoadingType.PULL_REFRESH) {
                    searchUiState.pullToRefreshState.updateRefreshState(RefreshIndicatorState.Default)
                }
                searchUiState = searchUiState.copy(dataLoadingUiState = DataLoadingUiState.Success)
            } catch (e: IOException) {
                searchUiState.pullToRefreshState.updateRefreshState(RefreshIndicatorState.Default)
                searchUiState = searchUiState.copy(dataLoadingUiState = DataLoadingUiState.Error)
            } catch (e: HttpException) {
                searchUiState.pullToRefreshState.updateRefreshState(RefreshIndicatorState.Default)
                searchUiState = searchUiState.copy(dataLoadingUiState = DataLoadingUiState.Error)
            } finally {
                searchUiState = searchUiState.copy(isLoading = false)
            }
        }
    }

    fun searchPhotos(loadingType: DataLoadingUiState.LoadingType, query: String) {
        searchUiState = searchUiState.copy(loadingType = loadingType)
        currentPage =
            if (loadingType == DataLoadingUiState.LoadingType.INITIAL_LOAD || loadingType == DataLoadingUiState.LoadingType.PULL_REFRESH)
                1
            else
                currentPage.inc()
        loadPhotosJob?.cancel()
        searchPhotosJob?.cancel()
        searchPhotosJob = viewModelScope.launch {
            searchUiState = searchUiState.copy(
                isLoading = true,
                dataLoadingUiState = DataLoadingUiState.Loading
            )
            try {
                delay(2000)
                val newPhotos = repository.searchPhotos(query = query, page = currentPage)
                if (searchUiState.photos.isNotEmpty()
                    && (loadingType == DataLoadingUiState.LoadingType.INITIAL_LOAD || loadingType == DataLoadingUiState.LoadingType.PULL_REFRESH)
                )
                    searchUiState.photos.clear()
                searchUiState.photos.addAll(newPhotos)
                if (loadingType == DataLoadingUiState.LoadingType.PULL_REFRESH) {
                    searchUiState.pullToRefreshState.updateRefreshState(RefreshIndicatorState.Default)
                }
                searchUiState = searchUiState.copy(dataLoadingUiState = DataLoadingUiState.Success)
            } catch (e: IOException) {
                searchUiState.pullToRefreshState.updateRefreshState(RefreshIndicatorState.Default)
                searchUiState = searchUiState.copy(dataLoadingUiState = DataLoadingUiState.Error)
            } catch (e: HttpException) {
                searchUiState.pullToRefreshState.updateRefreshState(RefreshIndicatorState.Default)
                searchUiState = searchUiState.copy(dataLoadingUiState = DataLoadingUiState.Error)
            } finally {
                searchUiState = searchUiState.copy(isLoading = false)
            }
        }
    }

    private fun convertElapsedTimeIntoText(timeElapsed: Long): String {
        return DateUtils.getTimePassedInHourMinSec(resourceProvider, timeElapsed)
    }
}

data class SearchUiState(
    val dataLoadingUiState: DataLoadingUiState = DataLoadingUiState.Loading,
    val photos: MutableList<Post> = mutableListOf(),
    val isLoading: Boolean = false,
    val loadingType: DataLoadingUiState.LoadingType = DataLoadingUiState.LoadingType.INITIAL_LOAD,
    val pullToRefreshState: PullToRefreshLayoutState
)

sealed interface DataLoadingUiState {
    data object Success : DataLoadingUiState
    data object Error : DataLoadingUiState
    data object Loading : DataLoadingUiState

    enum class LoadingType {
        INITIAL_LOAD, PULL_REFRESH, LOAD_MORE
    }
}