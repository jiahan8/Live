package com.example.live.data.repository

import com.example.live.data.model.Post

interface PhotosRepository {
    suspend fun getPhotos(page: Int): List<Post>
    suspend fun searchPhotos(query: String, page: Int): List<Post>
}