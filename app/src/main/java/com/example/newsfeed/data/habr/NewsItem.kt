package com.example.newsfeed.data.habr

import com.example.newsfeed.data.NewsItemInterface
import com.example.newsfeed.data.NewsSource
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
data class NewsItem @JvmOverloads constructor(
    @field:Element(name = "title")
    @param:Element(name = "title")
    override val title: String = "",

    @field:Element(name = "link")
    @param:Element(name = "link")
    override val link: String = "",

    @field:Element(name = "pubDate")
    @param:Element(name = "pubDate")
    override val pubDate: String = "",

    @field:Element(name = "description")
    @param:Element(name = "description")
    override val description: String = ""
) : NewsItemInterface {
    override val imageUrl: String
        get() {
            val imgPattern = "<img.*?src=\"(.*?)\"".toRegex()
            val matchResult = imgPattern.find(description)
            return matchResult?.groups?.get(1)?.value ?: ""
        }

    override val source: NewsSource
        get() = NewsSource.HABR
}