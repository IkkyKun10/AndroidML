package com.dicoding.asclepius.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.dicoding.asclepius.data.remote.NewsResponse
import com.dicoding.asclepius.domain.News
import com.dicoding.asclepius.domain.NewsRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

/**
 * @author riezky maisyar
 */

class NewsRepositoryImpl(private val client: HttpClient) : NewsRepository {
    override fun getNews(context: Context): Flow<List<News>> {
        return flow {
            try {
//                val response = newsApi.getNews()
                val response = client.get("top-headlines").body<NewsResponse>()

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
                    Log.i("NewsRepositoryImplR", "getNews: ${e.message}")
                } else {
                    emit(emptyList())
                    Toast.makeText(
                        context,
                        "Exception with message: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.i("NewsRepositoryImpl", "getNews: ${e.message}")
                }
            }
        }
    }
}