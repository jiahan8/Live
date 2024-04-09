package com.example.live.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.live.data.model.Post
import com.example.live.search.DataLoadingUiState
import com.example.live.ui.pullrefresh.PullToRefreshLayout

@Composable
internal fun HomeRoute(
    onPostClick: (Post) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen(
        homeViewModel.homeUiState,
        onPostClick,
        { homeViewModel.loadPosts(DataLoadingUiState.LoadingType.LOAD_MORE) },
        { homeViewModel.loadPosts(DataLoadingUiState.LoadingType.PULL_REFRESH) },
    )
}

@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    onPostClick: (Post) -> Unit,
    onLoadMore: () -> Unit,
    onPullRefresh: () -> Unit
) {
    Posts(
        homeUiState,
        onPostClick,
        onLoadMore,
        onPullRefresh,
    )
}

@Composable
fun Posts(
    homeUiState: HomeUiState,
    onPostClick: (Post) -> Unit,
    onLoadMore: () -> Unit,
    onPullRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val posts = rememberSaveable { homeUiState.posts }
    val lazyListState = rememberLazyListState()
    val pullToRefreshState = remember {
        homeUiState.pullToRefreshState
    }

    val shouldLoadMore by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex +
                    lazyListState.layoutInfo.visibleItemsInfo.size >= posts.size
        }
    }
    if (shouldLoadMore && !homeUiState.isLoading) {
        onLoadMore()
    }

    PullToRefreshLayout(
        pullRefreshLayoutState = pullToRefreshState,
        onRefresh = {
            onPullRefresh()
        },
    ) {
        Column {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                state = lazyListState,
                content = {
                    items(items = posts) { post ->
                        Column(
                            modifier = modifier
                                .clickable {
                                    onPostClick(post)
                                }
                                .padding(top = 20.dp, bottom = 20.dp)
                        ) {
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .padding(start = 20.dp, end = 20.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 8.dp,
                                            topEnd = 8.dp,
                                            bottomStart = 8.dp,
                                            bottomEnd = 8.dp
                                        )
                                    ),
                                painter = rememberAsyncImagePainter(post.imageUrl),
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )
                            post.title?.let {
                                Text(
                                    text = post.title,
                                    modifier = modifier.padding(
                                        start = 20.dp,
                                        top = 10.dp,
                                        end = 20.dp
                                    )
                                )
                            }
                        }
                        HorizontalDivider(thickness = 1.dp)
                    }

                    if (homeUiState.isLoading && homeUiState.loadingType != DataLoadingUiState.LoadingType.PULL_REFRESH) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 30.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            )
        }
    }
}