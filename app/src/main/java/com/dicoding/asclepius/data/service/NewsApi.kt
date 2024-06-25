package com.dicoding.asclepius.data.service

import com.dicoding.asclepius.data.remote.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author riezky maisyar
 */

interface NewsApi {

    @GET("top-headlines")
    suspend fun getNews(
        @Query("country") country: String = "id",
        @Query("category") category: String = "health",
        @Query("apiKey") apiKey: String = API_KEY
    ) : NewsResponse

//    @GET("top-headlines?country={country}&category={category}&apiKey={apiKey}")
//    suspend fun getNews(
//        @Path("country") country: String = "id",
//        @Path("category") category: String = "health",
//        @Path("apiKey") apiKey: String = API_KEY
//    ) : NewsResponse


    companion object {
        const val BASE_URL = "https://newsapi.org/v2/"
        const val API_KEY = "45e694f7d98240db88c7094a9d596091"
    }
}