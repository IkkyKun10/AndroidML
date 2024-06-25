package com.dicoding.asclepius.view.news

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.domain.NewsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

/**
 * @author riezky maisyar
 */

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    fun getNews(context: Context) = newsRepository.getNews(context)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )
}