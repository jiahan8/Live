package com.example.live.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.live.database.model.Post
import com.example.live.datastore.UserPreferences
import com.example.live.network.LiveNetworkDataSource
import com.example.live.network.model.NetworkPost
import com.example.live.network.model.asEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

interface LiveRepository {
    suspend fun getPhotosFeed(page: Int): List<Post>
    suspend fun searchPhotos(query: String, page: Int): List<Post>
    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun updateIsDarkTheme(isDarkTheme: Boolean)
}

private const val USER_PREFERENCES_NAME = "user_preferences"

private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class PhotoRepository @Inject constructor(
    private val dataSource: LiveNetworkDataSource,
    @ApplicationContext private val context: Context,
) : LiveRepository {

    override suspend fun getPhotosFeed(page: Int): List<Post> =
        dataSource.getPhotos(page).map(NetworkPost::asEntity)

    override suspend fun searchPhotos(query: String, page: Int): List<Post> =
        dataSource.searchPhotos(query, page).results?.map(NetworkPost::asEntity) ?: emptyList()

    override val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            // Get our dark theme value, defaulting to false if not set:
            val isDarkTheme = preferences[PreferencesKeys.IS_DARK_THEME] ?: false
            UserPreferences(isDarkTheme)
        }

    override suspend fun updateIsDarkTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_THEME] = isDarkTheme
        }
    }
}

private object PreferencesKeys {
    val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
}