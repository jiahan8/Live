package com.example.live.data.repository

import com.example.live.data.model.Post
import com.example.live.network.LiveNetworkDataSource
import com.example.live.network.model.NetworkPost
import com.example.live.network.model.asEntity
import javax.inject.Inject

class SearchContentPhotosRepository @Inject constructor(
    private val dataSource: LiveNetworkDataSource,
) : PhotosRepository {

    override suspend fun getPhotos(page: Int): List<Post> =
        dataSource.getPhotos(page).map(NetworkPost::asEntity)

    override suspend fun searchPhotos(query: String, page: Int): List<Post> =
        dataSource.searchPhotos(query, page).results?.map(NetworkPost::asEntity) ?: emptyList()
}