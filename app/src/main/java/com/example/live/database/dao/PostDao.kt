package com.example.live.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.live.database.model.DatabasePost
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("select * from posts")
    fun getPosts(): Flow<List<DatabasePost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(vararg posts: DatabasePost)

    @Query("delete from posts")
    suspend fun deletePosts()

    /**
     * So that autogenerated key for DatabasePost table will start from 1
     */
    @Query("delete from sqlite_sequence where name='posts'")
    suspend fun deleteSequence()
}