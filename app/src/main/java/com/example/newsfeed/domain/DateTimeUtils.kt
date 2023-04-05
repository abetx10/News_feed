package com.example.newsfeed.domain

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

object DateTimeUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun String.toLocalDateTime(): LocalDateTime {
        val formatters = listOf(
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm", Locale("ru"))
        )

        for (formatter in formatters) {
            try {
                return LocalDateTime.parse(this, formatter)
            } catch (e: DateTimeParseException) {
                // Do nothing, try the next formatter
            }
        }

        throw DateTimeParseException("Unsupported date format: $this", this, 0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun LocalDateTime.toFormattedString(): String {
        val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm")
        return this.format(formatter)
    }
}