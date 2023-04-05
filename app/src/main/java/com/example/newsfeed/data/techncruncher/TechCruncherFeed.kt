package com.example.newsfeed.data.techncruncher

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class TechCruncherFeed @JvmOverloads constructor(
    @field:Element(name = "channel")
    @param:Element(name = "channel")
    val channel: TechCruncherChannel = TechCruncherChannel()
)