package com.example.newsfeed.data

interface NewsItemInterface {
    val title: String
    val link: String
    val pubDate: String
    val description: String
    val imageUrl: String
    val source: NewsSource
}