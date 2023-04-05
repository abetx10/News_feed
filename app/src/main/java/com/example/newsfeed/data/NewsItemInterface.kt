package com.example.newsfeed.data

import com.example.newsfeed.data.NewsSource

interface NewsItemInterface {
    val title: String
    val link: String
    val pubDate: String
    val description: String
    val imageUrl: String
    val source: NewsSource
}