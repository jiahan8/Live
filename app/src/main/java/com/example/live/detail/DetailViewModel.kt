package com.example.live.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.live.detail.navigation.DETAIL_POST_INFO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val post = savedStateHandle.getStateFlow<String?>(
        key = DETAIL_POST_INFO,
        null
    ).value
}