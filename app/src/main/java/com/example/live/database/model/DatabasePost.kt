package com.example.live.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.live.data.model.Post
import kotlinx.parcelize.Parcelize

/**
 * Post Table in Room Database
 */
@Parcelize
@Entity(tableName = "posts")
data class DatabasePost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "post_id") val postId: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
) : Parcelable

fun List<DatabasePost>.asEntity(): List<Post> {
    return map {
        Post(
            id = it.postId,
            title = it.title,
            imageUrl = it.imageUrl,
        )
    }
}