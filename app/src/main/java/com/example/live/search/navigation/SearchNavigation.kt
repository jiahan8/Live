package com.example.live.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.live.database.model.Post
import com.example.live.search.SearchRoute

const val SEARCH_ROUTE = "search_route"
private const val DEEP_LINK_URI_PATTERN = "live://jiahan8.github.io/search"

fun NavController.navigateToSearch(navOptions: NavOptions) = navigate(SEARCH_ROUTE, navOptions)

fun NavGraphBuilder.searchScreen(onPostClick: (Post) -> Unit) {
    composable(
        route = SEARCH_ROUTE,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DEEP_LINK_URI_PATTERN
            }
        )
    ) {
        SearchRoute(onPostClick = onPostClick)
    }
}