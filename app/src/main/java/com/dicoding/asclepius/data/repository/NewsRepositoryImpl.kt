package com.dicoding.asclepius.data.repository

import android.content.Context
import android.widget.Toast
import com.dicoding.asclepius.data.service.NewsApi
import com.dicoding.asclepius.domain.News
import com.dicoding.asclepius.domain.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

/**
 * @author riezky maisyar
 */

class NewsRepositoryImpl(private val newsApi: NewsApi) : NewsRepository {
    override fun getNews(context: Context): Flow<List<News>> {
        return flow {
            try {
                val response = newsApi.getNews()
                val articles = response.articles?.map { it.toNewsDomain() }
                articles?.let {
                    emit(it)
                }
            } catch (e: Exception) {
                if (e is HttpException) {
                    emit(emptyList())
                    Toast.makeText(
                        context,
                        "HttpException with code: ${e.code()} & message: ${e.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    emit(emptyList())
                    Toast.makeText(
                        context,
                        "Exception with message: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}