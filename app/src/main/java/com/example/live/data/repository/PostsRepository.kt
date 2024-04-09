package com.example.live.data.repository

import com.example.live.database.model.Post
import kotlinx.coroutines.flow.Flow

interface PostsRepository {
    val posts: Flow<List<Post>>
    suspend fun savePosts(page: Int)
}