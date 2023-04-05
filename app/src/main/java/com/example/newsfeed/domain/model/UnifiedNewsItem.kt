package com.example.newsfeed.domain.model

import com.example.newsfeed.data.NewsSource

data class UnifiedNewsItem(
    val title: String,
    val link: String,
    val pubDate: String,
    val description: String,
    val imageUrl: String? = null,
    val source: NewsSource,
    var isFavorite: Boolean = false
)