package com.example.live.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceProvider @Inject constructor(@ApplicationContext val context: Context) {
    fun getString(id: Int, vararg args: Any) = context.getString(id, *args)
}