package com.example.live.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.live.database.model.Post
import com.example.live.search.SearchRoute

const val SEARCH_ROUTE = "search_route"

fun NavController.navigateToSearch(navOptions: NavOptions) = navigate(SEARCH_ROUTE, navOptions)

fun NavGraphBuilder.searchScreen(onPostClick: (Post) -> Unit) {
    composable(
        route = SEARCH_ROUTE
    ) {
        SearchRoute(onPostClick = onPostClick)
    }
}