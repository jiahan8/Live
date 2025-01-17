package com.example.live.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.live.R
import com.example.live.data.model.Post
import com.example.live.ui.pullrefresh.PullToRefreshLayout

@Composable
internal fun SearchRoute(
    onPostClick: (Post) -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    SearchScreen(
        searchViewModel.searchUiState,
        onPostClick,
        searchViewModel::loadPhotos,
        searchViewModel::searchPhotos,
    )
}

@Composable
fun SearchScreen(
    searchUiState: SearchUiState,
    onPostClick: (Post) -> Unit,
    onLoadPhotos: (DataLoadingUiState.LoadingType) -> Unit,
    onSearchPhotos: (DataLoadingUiState.LoadingType, String) -> Unit
) {
    Photos(
        searchUiState,
        onPostClick,
        onLoadPhotos,
        onSearchPhotos,
    )
}

@Composable
fun Photos(
    searchUiState: SearchUiState,
    onPostClick: (Post) -> Unit,
    onLoadPhotos: (DataLoadingUiState.LoadingType) -> Unit,
    onSearchPhotos: (DataLoadingUiState.LoadingType, String) -> Unit
) {
    val photos = rememberSaveable { searchUiState.photos }
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()
    val pullToRefreshState = remember {
        searchUiState.pullToRefreshState
    }
    val shouldLoadMore by remember {
        derivedStateOf {
            lazyStaggeredGridState.firstVisibleItemIndex +
                    lazyStaggeredGridState.layoutInfo.visibleItemsInfo.size >= photos.size
        }
    }
    var searchText by rememberSaveable { mutableStateOf("") }

    if (shouldLoadMore && !searchUiState.isLoading) {
        if (searchText.isNotBlank()) {
            onSearchPhotos(DataLoadingUiState.LoadingType.LOAD_MORE, searchText)
        } else {
            onLoadPhotos(DataLoadingUiState.LoadingType.LOAD_MORE)
        }
    }

    Scaffold(
        topBar = {
            TextField(
                value = searchText,
                onValueChange = { text ->
                    searchText = text
                    if (searchText.isNotBlank()) {
                        onSearchPhotos(DataLoadingUiState.LoadingType.INITIAL_LOAD, searchText)
                    } else {
                        onLoadPhotos(DataLoadingUiState.LoadingType.INITIAL_LOAD)
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(size = 12.dp),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                placeholder = { Text(text = stringResource(id = R.string.search)) },
                colors = TextFieldDefaults.colors(
                    cursorColor = Color.Gray,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                )
            )
        }
    ) { innerPadding ->
        PullToRefreshLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            pullRefreshLayoutState = pullToRefreshState,
            onRefresh = {
                if (searchText.isNotBlank()) {
                    onSearchPhotos(DataLoadingUiState.LoadingType.PULL_REFRESH, searchText)
                } else {
                    onLoadPhotos(DataLoadingUiState.LoadingType.PULL_REFRESH)
                }
            },
        ) {
            Box {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = 2.dp,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    state = lazyStaggeredGridState,
                    content = {
                        items(items = photos) { photo ->
                            AsyncImage(
                                model = photo.imageUrl,
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable {
                                        onPostClick(photo)
                                    }
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                )

                if (searchUiState.isLoading && searchUiState.loadingType != DataLoadingUiState.LoadingType.PULL_REFRESH) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(30.dp)
                    )
                }
            }
        }
    }
}