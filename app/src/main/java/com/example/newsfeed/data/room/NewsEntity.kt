package com.example.newsfeed.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey
    val url: String,
    val source: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val pubDate: String,
    val isFavorite: Boolean
)