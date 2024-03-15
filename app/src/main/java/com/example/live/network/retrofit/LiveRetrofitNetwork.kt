package com.example.live.network.retrofit

import androidx.tracing.trace
import com.example.live.BuildConfig
import com.example.live.network.LiveNetworkDataSource
import com.example.live.network.model.NetworkPost
import com.example.live.network.model.NetworkSearchPost
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrofit API declaration for LIVE Network API
 */
private interface LiveRetrofitNetworkApi {
    @GET(value = "photos")
    suspend fun getPhotos(
        @Query("client_id") accessKey: String,
        @Query("page") page: Int
    ): List<NetworkPost>

    @GET(value = "search/photos")
    suspend fun searchPhotos(
        @Query("client_id") accessKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): NetworkSearchPost
}

private const val LIVE_BASE_URL = "https://api.unsplash.com/"

/**
 * [Retrofit] backed [LiveNetworkDataSource]
 */
@Singleton
internal class LiveRetrofitNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : LiveNetworkDataSource {

    private val networkApi = trace("LiveRetrofitNetwork") {
        Retrofit.Builder()
            .baseUrl(LIVE_BASE_URL)
            // We use callFactory lambda here with dagger.Lazy<Call.Factory>
            // to prevent initializing OkHttp on the main thread.
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(LiveRetrofitNetworkApi::class.java)
    }

    override suspend fun getPhotos(page: Int): List<NetworkPost> =
        networkApi.getPhotos(BuildConfig.ACCESS_KEY, page)

    override suspend fun searchPhotos(query: String, page: Int): NetworkSearchPost =
        networkApi.searchPhotos(BuildConfig.ACCESS_KEY, query, page)
}