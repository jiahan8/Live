package com.example.live.ui.pullrefresh

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow

class PullToRefreshLayoutState(
    val onTimeUpdated: (Long) -> String,
) {

    private val _lastRefreshTime: MutableStateFlow<Long> =
        MutableStateFlow(System.currentTimeMillis())

    var refreshIndicatorState = mutableStateOf(RefreshIndicatorState.Default)
        private set

    var lastRefreshText = mutableStateOf("")
        private set

    fun updateRefreshState(refreshState: RefreshIndicatorState) {
        val now = System.currentTimeMillis()
        val timeElapsed = now - _lastRefreshTime.value
        lastRefreshText.value = onTimeUpdated(timeElapsed)
        refreshIndicatorState.value = refreshState
    }

    fun refresh() {
        _lastRefreshTime.value = System.currentTimeMillis()
        updateRefreshState(RefreshIndicatorState.Refreshing)
    }
}