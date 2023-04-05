package com.example.newsfeed.data

import com.example.newsfeed.data.habr.Feed
import com.example.newsfeed.data.habr.NewsApi
import com.example.newsfeed.data.techncruncher.TechCrunchNewsApi
import com.example.newsfeed.data.techncruncher.TechCruncherFeed
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsApi: NewsApi, private val techCrunchNewsApi: TechCrunchNewsApi) {
    suspend fun getHabrFeed(): Response<Feed> {
        return newsApi.getHabrFeed()
    }

    suspend fun getTechCrunchFeed(): Response<TechCruncherFeed> {
        return techCrunchNewsApi.getTechCrunchFeed()
    }
}