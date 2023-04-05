package com.example.newsfeed.data

enum class NewsSource(val displayName: String) {
    HABR("Habr"),
    TECHCRUNCHER("TechCruncher");

    companion object {
        fun fromDisplayName(displayName: String): NewsSource? {
            return values().find { it.displayName == displayName }
        }
    }
}