package com.example.live.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.live.data.model.Post
import com.example.live.home.HomeRoute

const val HOME_ROUTE = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(HOME_ROUTE, navOptions)

fun NavGraphBuilder.homeScreen(onPostClick: (Post) -> Unit) {
    composable(
        route = HOME_ROUTE
    ) {
        HomeRoute(onPostClick = onPostClick)
    }
}