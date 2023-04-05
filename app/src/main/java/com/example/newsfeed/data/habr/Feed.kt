package com.example.newsfeed.data.habr

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class Feed @JvmOverloads constructor(
    @field:Element(name = "channel")
    @param:Element(name = "channel")
    val channel: Channel = Channel()
)