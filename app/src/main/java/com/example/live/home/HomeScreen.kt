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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.live.database.model.Post
import com.example.live.search.SearchUiState
import com.example.live.ui.pullrefresh.PullToRefreshLayout

@Composable
internal fun HomeRoute(
    onPostClick: (Post) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen(
        homeViewModel,
        onPostClick,
        { homeViewModel.loadPhotos(SearchUiState.LoadingType.LOAD_MORE) },
        { homeViewModel.loadPhotos(SearchUiState.LoadingType.PULL_REFRESH) },
    )
}

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onPostClick: (Post) -> Unit,
    onLoadMore: () -> Unit,
    onPullRefresh: () -> Unit
) {
    Posts(
        homeViewModel.posts,
        onPostClick,
        onLoadMore,
        onPullRefresh,
        homeViewModel
    )
}

@Composable
fun Posts(
    posts: List<Post>,
    onPostClick: (Post) -> Unit,
    onLoadMore: () -> Unit,
    onPullRefresh: () -> Unit,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val photos = rememberSaveable { posts }
    val lazyListState = rememberLazyListState()
    val pullToRefreshState = remember {
        homeViewModel.pullToRefreshState
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

                    if (homeViewModel.isLoading && homeViewModel.loadingType != SearchUiState.LoadingType.PULL_REFRESH) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    val shouldLoadMore =
                        lazyListState.firstVisibleItemIndex +
                                lazyListState.layoutInfo.visibleItemsInfo.size >= photos.size
                    if (shouldLoadMore && !homeViewModel.isLoading) {
                        onLoadMore()
                    }
                }
            )
        }
    }
}