package com.example.live.network

import com.example.live.network.model.NetworkPost
import com.example.live.network.model.NetworkSearchPost

/**
 * Interface representing network calls to the LIVE backend
 */
interface LiveNetworkDataSource {
    suspend fun getPhotos(page: Int): List<NetworkPost>
    suspend fun searchPhotos(query: String, page: Int): NetworkSearchPost
}