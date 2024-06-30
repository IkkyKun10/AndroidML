package com.dicoding.asclepius.data.di

import android.content.Context
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.repository.NewsRepositoryImpl
import com.dicoding.asclepius.data.service.NewsApi
import com.dicoding.asclepius.domain.NewsRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

interface AppModule {
    val newsApi: NewsApi
    val ktorClient: HttpClient
    val newsRepository: NewsRepository
}

class AppModuleImpl(
    private val context: Context
) : AppModule {
    override val ktorClient by lazy {
        HttpClient(CIO) {
            expectSuccess = true
            engine {
                endpoint {
                    keepAliveTime = 5000
                    connectTimeout = 5000
                    connectAttempts = 3
                }
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "newsapi.org"
//                    path("top-headlines")
                    path("v2/")
                    parameters.append("country", "id")
                    parameters.append("category", "health")
                    parameters.append("apiKey", BuildConfig.API_KEY)
                }
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("Ktor => $message")
                    }
                }

                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json()
            }
        }
    }



    override val newsApi: NewsApi by lazy {
        Retrofit.Builder()
            .baseUrl(NewsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    override val newsRepository: NewsRepository by lazy {
        NewsRepositoryImpl(ktorClient)
    }

}