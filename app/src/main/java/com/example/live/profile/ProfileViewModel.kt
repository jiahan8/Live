package com.example.live.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.live.data.repository.LiveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: LiveRepository
) : ViewModel() {

    val userPreferences = runBlocking { repository.userPreferencesFlow.first() }

    fun updateIsDarkTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            repository.updateIsDarkTheme(isDarkTheme)
        }
    }
}