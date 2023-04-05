package com.example.newsfeed.data.habr

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "channel", strict = false)
data class Channel @JvmOverloads constructor(
    @field:ElementList(entry = "item", inline = true, required = false)
    @param:ElementList(entry = "item", inline = true, required = false)
    val items: List<NewsItem> = mutableListOf(),

    @field:Element(name = "title")
    @param:Element(name = "title")
    val title: String = "",

    @field:Element(name = "link")
    @param:Element(name = "link")
    val link: String = "",

    @field:Element(name = "description")
    @param:Element(name = "description")
    val description: String = "",

    @field:Element(name = "language")
    @param:Element(name = "language")
    val language: String = "",
)