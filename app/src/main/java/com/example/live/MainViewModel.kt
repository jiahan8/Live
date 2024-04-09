package com.example.live

import androidx.lifecycle.ViewModel
import com.example.live.data.repository.ProfileUserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: ProfileUserDataRepository
) : ViewModel() {

    val userPreferencesFlow = repository.userPreferencesFlow

}