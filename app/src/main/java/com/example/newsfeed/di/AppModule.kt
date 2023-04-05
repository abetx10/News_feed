package com.example.newsfeed.di

import com.example.newsfeed.data.NewsRepository
import com.example.newsfeed.data.habr.NewsApi
import com.example.newsfeed.data.room.AppDatabase
import com.example.newsfeed.data.techncruncher.TechCrunchNewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideHabrNewsApi(okHttpClient: OkHttpClient): NewsApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://habr.com/ru/rss/all/all/?fl=ru")
            .client(okHttpClient)
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
            .build()

        return retrofit.create(NewsApi::class.java)
    }

    @Provides
    fun provideTechCrunchNewsApi(okHttpClient: OkHttpClient): TechCrunchNewsApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://feeds.feedburner.com/")
            .client(okHttpClient)
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
            .build()

        return retrofit.create(TechCrunchNewsApi::class.java)
    }

    @Provides
    fun provideNewsRepository(
        habrNewsApi: NewsApi,
        techCrunchNewsApi: TechCrunchNewsApi,
        appDatabase: AppDatabase
    ): NewsRepository {
        return NewsRepository(habrNewsApi, techCrunchNewsApi, appDatabase)
    }
}