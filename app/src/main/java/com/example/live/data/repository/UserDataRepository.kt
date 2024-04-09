package com.example.live.data.repository

import com.example.live.datastore.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun updateIsDarkTheme(isDarkTheme: Boolean)
}