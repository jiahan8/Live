package com.example.live.data.repository

import com.example.live.database.model.Post
import com.example.live.network.LiveNetworkDataSource
import com.example.live.network.model.NetworkPost
import com.example.live.network.model.asEntity
import javax.inject.Inject

interface LiveRepository {
    suspend fun getPhotosFeed(page: Int): List<Post>
    suspend fun searchPhotosFeed(query: String, page: Int): List<Post>
}

class PhotoRepository @Inject constructor(
    private val dataSource: LiveNetworkDataSource,
) : LiveRepository {

    override suspend fun getPhotosFeed(page: Int): List<Post> =
        dataSource.getPhotos(page).map(NetworkPost::asEntity)

    override suspend fun searchPhotosFeed(query: String, page: Int): List<Post> =
        dataSource.searchPhotos(query, page).results?.map(NetworkPost::asEntity) ?: emptyList()
}