package com.example.live

import androidx.lifecycle.ViewModel
import com.example.live.data.repository.LiveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: LiveRepository
) : ViewModel() {

    val userPreferencesFlow = repository.userPreferencesFlow

}