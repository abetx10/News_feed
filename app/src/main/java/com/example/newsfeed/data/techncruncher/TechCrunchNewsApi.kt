package com.example.newsfeed.data.techncruncher

import retrofit2.Response
import retrofit2.http.GET

interface TechCrunchNewsApi {
    @GET("techcrunch")
    suspend fun getTechCrunchFeed(): Response<TechCruncherFeed>
}