package com.dicoding.asclepius.domain

import android.content.Context
import kotlinx.coroutines.flow.Flow

/**
 * @author riezky maisyar
 */

interface NewsRepository {
    fun getNews(context: Context) : Flow<List<News>>
}