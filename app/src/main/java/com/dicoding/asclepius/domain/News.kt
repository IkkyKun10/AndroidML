package com.dicoding.asclepius.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    val publishedAt: String?,
    val author: String?,
    val urlToImage: String?,
    val description: String?,
    val title: String?,
    val url: String?,
    val content: String?,
    val name: String?,
    val id: String?,
) : Parcelable
