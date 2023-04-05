import com.example.newsfeed.data.NewsItemInterface
import com.example.newsfeed.data.NewsSource
import com.example.newsfeed.data.habr.NewsItem
import com.example.newsfeed.data.room.NewsEntity
import com.example.newsfeed.data.techncruncher.TechCruncherItem
import com.example.newsfeed.domain.model.UnifiedNewsItem

object NewsMapper {
    fun toNewsEntity(item: NewsItemInterface): NewsEntity {
        return NewsEntity(
            url = item.link,
            source = item.source.name,
            title = item.title,
            description = item.description,
            imageUrl = item.imageUrl,
            pubDate = item.pubDate,
            isFavorite = false
        )
    }

    fun toUnifiedNewsItem(entity: NewsEntity): UnifiedNewsItem {
        return UnifiedNewsItem(
            link = entity.url,
            source = NewsSource.valueOf(entity.source),
            title = entity.title,
            description = entity.description,
            imageUrl = entity.imageUrl,
            pubDate = entity.pubDate,
            isFavorite = entity.isFavorite
        )
    }

    fun TechCruncherItem.toUnifiedNewsItem(): UnifiedNewsItem {
        return toUnifiedNewsItem(toNewsEntity(this))
    }

    fun NewsItem.toUnifiedNewsItem(): UnifiedNewsItem {
        return toUnifiedNewsItem(toNewsEntity(this))
    }
}