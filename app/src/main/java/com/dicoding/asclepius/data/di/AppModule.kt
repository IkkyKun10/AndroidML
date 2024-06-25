package com.dicoding.asclepius.data.di

import android.content.Context
import com.dicoding.asclepius.data.repository.NewsRepositoryImpl
import com.dicoding.asclepius.data.service.NewsApi
import com.dicoding.asclepius.domain.NewsRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

interface AppModule {
    val newsApi: NewsApi
    val newsRepository: NewsRepository
}

class AppModuleImpl(
    private val context: Context
) : AppModule {
    override val newsApi: NewsApi by lazy {
        Retrofit.Builder()
            .baseUrl(NewsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    override val newsRepository: NewsRepository by lazy {
        NewsRepositoryImpl(newsApi)
    }

}