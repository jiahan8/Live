package com.example.live.network.model

import com.example.live.database.model.DatabasePost
import com.example.live.data.model.Post
import kotlinx.serialization.Serializable

/**
 * Network representation of [Post]
 */
@Serializable
data class NetworkPost(
    val id: String?,
    val description: String?,
    val urls: Urls,
)

@Serializable
data class Urls(
    val raw: String?,
    val full: String?,
    val regular: String?,
)

@Serializable
data class NetworkSearchPost(
    val results: List<NetworkPost>?,
)

fun NetworkPost.asEntity() = Post(
    id = id,
    title = description,
    imageUrl = urls.regular
)

fun List<NetworkPost>.asDatabaseModel(): Array<DatabasePost> = this.map {
    DatabasePost(
        postId = it.id,
        title = it.description,
        imageUrl = it.urls.regular
    )
}.toTypedArray()