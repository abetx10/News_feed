package com.example.newsfeed.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.newsfeed.data.NewsItemInterface
import com.example.newsfeed.domain.DateTimeUtils.toFormattedString
import com.example.newsfeed.domain.DateTimeUtils.toLocalDateTime
import com.example.newsfeed.domain.model.UnifiedNewsItem

@RequiresApi(Build.VERSION_CODES.O)
fun NewsItemInterface.toUnifiedNewsItem(): UnifiedNewsItem {
    return UnifiedNewsItem(
        title = this.title,
        link = this.link,
        pubDate = this.pubDate.toLocalDateTime().toFormattedString(),
        description = this.description,
        imageUrl = this.imageUrl,
        source = this.source
    )
}