package com.example.live.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.live.detail.navigation.detailScreen
import com.example.live.home.navigation.HOME_ROUTE
import com.example.live.home.navigation.homeScreen
import com.example.live.profile.navigation.profileScreen
import com.example.live.search.navigation.searchScreen
import com.example.live.ui.LiveAppState

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun LiveNavHost(
    appState: LiveAppState,
    modifier: Modifier = Modifier,
    startDestination: String = HOME_ROUTE,
) {
    val navController = appState.navController
    val stableOnPostClick by rememberUpdatedState(appState::navigateToDetail)
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeScreen(onPostClick = appState::navigateToDetail)
        searchScreen(onPostClick = stableOnPostClick)
        profileScreen()
        detailScreen(onBackClick = navController::popBackStack)
    }
}