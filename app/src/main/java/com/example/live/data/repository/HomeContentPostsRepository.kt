package com.example.live.data.repository

import com.example.live.database.PostDAO
import com.example.live.database.asDomainModel
import com.example.live.database.model.Post
import com.example.live.network.LiveNetworkDataSource
import com.example.live.network.model.asDatabaseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeContentPostsRepository @Inject constructor(
    private val dataSource: LiveNetworkDataSource,
    private val postDAO: PostDAO
) : PostsRepository {

    override val posts: Flow<List<Post>> =
        postDAO.getPosts().map { it.asDomainModel() }

    override suspend fun savePosts(page: Int) {
        val postList = dataSource.getPhotos(page)
        if (page == 1) {
            postDAO.deletePosts()
            postDAO.deleteSequence()
        }
        postDAO.insertPosts(*postList.asDatabaseModel())
    }
}