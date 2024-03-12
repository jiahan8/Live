package com.example.live.database.model

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val id: String?,
    val title: String?,
    val imageUrl: String?
) : Parcelable {
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}

//class PostType : NavType<Post>(isNullableAllowed = false) {
//    override fun get(bundle: Bundle, key: String): Post? {
//        return bundle.getParcelable(key)
//    }
//
//    override fun parseValue(value: String): Post {
//        return Gson().fromJson(value, Post::class.java)
//    }
//
//    override fun put(bundle: Bundle, key: String, value: Post) {
//        bundle.putParcelable(key, value)
//    }
//}

abstract class JsonNavType<T> : NavType<T>(isNullableAllowed = false) {
    abstract fun fromJsonParse(value: String): T
    abstract fun T.getJsonParse(): String

    override fun get(bundle: Bundle, key: String): T? =
        bundle.getString(key)?.let { parseValue(it) }

    override fun parseValue(value: String): T = fromJsonParse(value)

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, value.getJsonParse())
    }
}

class PostArgType : JsonNavType<Post>() {
    override fun fromJsonParse(value: String): Post = Gson().fromJson(value, Post::class.java)

    override fun Post.getJsonParse(): String = Gson().toJson(this)
}