package com.example.newsfeed.data.habr

import retrofit2.Response
import retrofit2.http.GET

interface NewsApi {
    @GET("rss.xml")
    suspend fun getHabrFeed(): Response<Feed>
}