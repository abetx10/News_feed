package com.example.newsfeed.data

import com.example.newsfeed.data.habr.Feed
import com.example.newsfeed.data.habr.NewsApi
import com.example.newsfeed.data.room.AppDatabase
import com.example.newsfeed.data.room.NewsEntity
import com.example.newsfeed.data.techncruncher.TechCrunchNewsApi
import com.example.newsfeed.data.techncruncher.TechCruncherFeed
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsApi: NewsApi, private val techCrunchNewsApi: TechCrunchNewsApi, private val appDatabase: AppDatabase) {
    suspend fun getHabrFeed(): Response<Feed> {
        return newsApi.getHabrFeed()
    }

    suspend fun getTechCrunchFeed(): Response<TechCruncherFeed> {
        return techCrunchNewsApi.getTechCrunchFeed()
    }

    suspend fun saveNewsToCache(news: List<NewsEntity>) {
        appDatabase.newsDao().insertAll(news)
    }

    suspend fun getCachedNews(source: NewsSource): List<NewsEntity> {
        return appDatabase.newsDao().getNewsBySource(source)
    }

    suspend fun clearNewsCache() {
        appDatabase.newsDao().deleteAll()
    }
}