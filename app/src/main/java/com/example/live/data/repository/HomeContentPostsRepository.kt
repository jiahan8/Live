package com.example.live.data.repository

import com.example.live.database.dao.PostDao
import com.example.live.database.model.asEntity
import com.example.live.data.model.Post
import com.example.live.network.LiveNetworkDataSource
import com.example.live.network.model.asDatabaseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeContentPostsRepository @Inject constructor(
    private val dataSource: LiveNetworkDataSource,
    private val postDao: PostDao
) : PostsRepository {

    override val posts: Flow<List<Post>> =
        postDao.getPosts().map { it.asEntity() }

    override suspend fun savePosts(page: Int) {
        val postList = dataSource.getPhotos(page)
        if (page == 1) {
            postDao.deletePosts()
            postDao.deleteSequence()
        }
        postDao.insertPosts(*postList.asDatabaseModel())
    }
}