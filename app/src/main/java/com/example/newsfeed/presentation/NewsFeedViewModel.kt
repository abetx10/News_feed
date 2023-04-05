package com.example.newsfeed.presentation

import NewsMapper
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.newsfeed.data.NewsRepository
import com.example.newsfeed.data.NewsSource
import com.example.newsfeed.domain.DateTimeUtils.toLocalDateTime
import com.example.newsfeed.domain.model.UnifiedNewsItem
import com.example.newsfeed.domain.toUnifiedNewsItem
import com.example.newsfeed.utils.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    @ApplicationContext private val application: Application
) :
    ViewModel() {

    private val _newsItems = MediatorLiveData<List<UnifiedNewsItem>>()
    val newsItems: LiveData<List<UnifiedNewsItem>> = _newsItems

    private val _techCrunchItems = MutableLiveData<List<UnifiedNewsItem>>()
    private val _habrItems = MutableLiveData<List<UnifiedNewsItem>>()

    private val _favoriteNewsItems = MutableLiveData<List<UnifiedNewsItem>>()
    val favoriteNewsItems: LiveData<List<UnifiedNewsItem>> = _favoriteNewsItems

    private var _selectedSourceValue = MutableLiveData<NewsSource?>(null)
    val selectedSourceValue: LiveData<NewsSource?> = _selectedSourceValue

    private var _newsDataChanged = MutableLiveData<Boolean>(false)

    init {
        fetchTechCrunchNews()
        fetchHabrNews()

        _techCrunchItems.observeForever {
            _newsDataChanged.postValue(true)
            combineNewsItems()
        }

        _habrItems.observeForever {
            _newsDataChanged.postValue(true)
            combineNewsItems()
        }

        _selectedSourceValue.observeForever { source ->
            _newsDataChanged.postValue(true)
        }

    }

    fun refreshNews() {
        fetchTechCrunchNews()
        fetchHabrNews()
    }

    private fun fetchTechCrunchNews() {
        viewModelScope.launch {
            if (isInternetAvailable(application)) {
                val response = newsRepository.getTechCrunchFeed()
                if (response.isSuccessful) {
                    response.body()?.channel?.items?.let { techCrunchItems ->
                        _techCrunchItems.postValue(techCrunchItems.map { it.toUnifiedNewsItem() })

                        newsRepository.saveNewsToCache(techCrunchItems.map {
                            NewsMapper.toNewsEntity(
                                it
                            )
                        })
                    }
                } else {
                    // Обработка ошибок
                }
            } else {
                val cachedNews = newsRepository.getCachedNews(NewsSource.TECHCRUNCHER)
                _techCrunchItems.postValue(cachedNews.map { NewsMapper.toUnifiedNewsItem(it) })
            }
        }
    }

    private fun fetchHabrNews() {
        viewModelScope.launch {
            if (isInternetAvailable(application)) {
                val response = newsRepository.getHabrFeed()
                if (response.isSuccessful) {
                    response.body()?.channel?.items?.let { habrItems ->
                        _habrItems.postValue(habrItems.map { it.toUnifiedNewsItem() })


                        newsRepository.saveNewsToCache(habrItems.map { NewsMapper.toNewsEntity(it) })
                    }
                } else {
                    // Обработка ошибок
                }
            } else {
                val cachedNews = newsRepository.getCachedNews(NewsSource.HABR)
                _habrItems.postValue(cachedNews.map { NewsMapper.toUnifiedNewsItem(it) })
            }
        }
    }

    fun setSelectedSource(source: NewsSource?) {
        if (_selectedSourceValue.value != source) {
            Log.d("NewsFeedViewModel", "Setting selected source: $source")
            _selectedSourceValue.value = source
            combineNewsItems()
        }
    }

    private fun updateFavoriteNewsItems() {
        val allNews = _newsItems.value ?: emptyList()
        val favoriteNews = allNews.filter { it.isFavorite }
        _favoriteNewsItems.postValue(favoriteNews)
    }

    fun toggleFavorite(unifiedNewsItem: UnifiedNewsItem, newFavoriteState: Boolean) {
        unifiedNewsItem.isFavorite = newFavoriteState
        updateFavoriteNewsItems()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun combineNewsItems() {
        if (_newsDataChanged.value != true) {
            return
        }

        val techCrunch = _techCrunchItems.value ?: emptyList()
        val habr = _habrItems.value ?: emptyList()

        if (techCrunch == null || habr == null) {
            return
        }

        val combined = techCrunch + habr
        Log.d("NewsFeedViewModel", "All news sources: ${combined.map { it.source }}")

        val selectedSource = _selectedSourceValue.value
        val filteredNews = if (selectedSource != null) {
            Log.d("NewsFeedViewModel", "Filtering news by source: $selectedSource")
            combined.filter { it.source == selectedSource }.also { filtered ->
                Log.d(
                    "NewsFeedViewModel",
                    "Filtered news: ${filtered.map { "${it.source.displayName}: ${it.title}" }}"
                )
            }
        } else {
            combined
        }

        _newsItems.value = filteredNews.sortedByDescending { it.pubDate.toLocalDateTime() }
        updateFavoriteNewsItems()
        _newsDataChanged.value = false

        Log.d("NewsFeedViewModel", "Combined news size: ${combined.size}")
        Log.d("NewsFeedViewModel", "Filtered news size: ${filteredNews.size}")
    }
}