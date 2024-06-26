package com.dicoding.asclepius.data.service

import com.dicoding.asclepius.data.remote.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query
import com.dicoding.asclepius.BuildConfig

/**
 * @author riezky maisyar
 */

interface NewsApi {

    @GET("top-headlines")
    suspend fun getNews(
        @Query("country") country: String = "id",
        @Query("category") category: String = "health",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ) : NewsResponse

    companion object {
        const val BASE_URL = "https://newsapi.org/v2/"
    }
}