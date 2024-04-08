package com.example.live.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.live.database.model.Post
import kotlinx.parcelize.Parcelize

/**
 * Post Table in Room Database
 */
@Parcelize
@Entity
data class DatabasePost(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val post_id: String?,
    val title: String?,
    val image_url: String?,
) : Parcelable

fun List<DatabasePost>.asDomainModel(): List<Post> {
    return map {
        Post(
            id = it.post_id,
            title = it.title,
            imageUrl = it.image_url,
        )
    }
}