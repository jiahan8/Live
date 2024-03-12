package com.example.live.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.live.database.model.Post
import com.example.live.ui.pullrefresh.PullToRefreshLayout

@Composable
internal fun SearchRoute(
    onPostClick: (Post) -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    SearchScreen(
        searchViewModel, onPostClick = onPostClick,
        { searchViewModel.loadPhotos(SearchUiState.LoadingType.LOAD_MORE) },
        { searchViewModel.loadPhotos(SearchUiState.LoadingType.PULL_REFRESH) },
    )
}

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    onPostClick: (Post) -> Unit,
    onLoadMore: () -> Unit,
    onPullRefresh: () -> Unit
) {
    Photos(
        searchViewModel.posts,
        onPostClick,
        onLoadMore,
        onPullRefresh,
        searchViewModel
    )
}

@Composable
fun Photos(
    posts: List<Post>,
    onPostClick: (Post) -> Unit,
    onLoadMore: () -> Unit,
    onPullRefresh: () -> Unit,
    searchViewModel: SearchViewModel
) {
    val photos = rememberSaveable { posts }
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()
    val pullToRefreshState = remember {
        searchViewModel.pullToRefreshState
    }

    PullToRefreshLayout(
        modifier = Modifier.fillMaxSize(),
        pullRefreshLayoutState = pullToRefreshState,
        onRefresh = {
            onPullRefresh()
        },
    ) {
        Box {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 2.dp,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                state = lazyStaggeredGridState,
                content = {
                    items(items = photos) { post ->
                        AsyncImage(
                            model = post.imageUrl,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable {
                                    onPostClick(post)
                                }
                                .fillMaxWidth()
                                .wrapContentHeight(),
                        )
                    }

                    val shouldLoadMore =
                        lazyStaggeredGridState.firstVisibleItemIndex +
                                lazyStaggeredGridState.layoutInfo.visibleItemsInfo.size >= photos.size
                    // Check if more data should be loaded and if not already loading
                    if (shouldLoadMore && !searchViewModel.isLoading) {
                        onLoadMore()
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )

            if (searchViewModel.isLoading && searchViewModel.loadingType != SearchUiState.LoadingType.PULL_REFRESH) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(30.dp)
                )
            }
        }
    }
}