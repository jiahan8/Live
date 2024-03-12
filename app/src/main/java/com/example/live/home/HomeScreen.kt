package com.example.live.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.live.database.model.Post
import com.example.live.ui.theme.LiveTheme

@Composable
internal fun HomeRoute(
    onPostClick: (Post) -> Unit,
) {
    HomeScreen(onPostClick)
}

@Composable
fun HomeScreen(onPostClick: (Post) -> Unit) {
    Posts(onPostClick = onPostClick)
}

@Composable
fun Posts(onPostClick: (Post) -> Unit, modifier: Modifier = Modifier) {
    val posts = listOf(
        Post("0", "me", "https://avatars.githubusercontent.com/u/19545570?v=4"),
        Post(
            "1",
            "kristen",
            "https://media.vanityfair.com/photos/5f6b7629ffc33aecb21a23df/master/pass/kristen.jpg"
        ),
        Post("2", "me", "https://avatars.githubusercontent.com/u/19545570?v=4"),
        Post(
            "3",
            "kristen",
            "https://media.vanityfair.com/photos/5f6b7629ffc33aecb21a23df/master/pass/kristen.jpg"
        ),
        Post("5", "me", "https://avatars.githubusercontent.com/u/19545570?v=4"),
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            posts.forEach {
                Post(it, onPostClick, modifier)
                HorizontalDivider(thickness = 1.dp)
            }
        }
    }
}

@Composable
fun Post(post: Post, onPostClick: (Post) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier
            .clickable {
                onPostClick(post)
            }
            .padding(top = 20.dp, bottom = 10.dp)
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(start = 20.dp, end = 12.dp)
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
        Text(
            text = post.title ?: "",
            modifier = modifier.padding(start = 20.dp, top = 10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LiveAppPreview() {
    LiveTheme {
        Posts({})
    }
}