package com.example.newsfeed.domain

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DateTimeUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun String.toLocalDateTime(): LocalDateTime {
        val formatter1 =
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH)
        val formatter2 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
        val formatter3 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm", Locale.ENGLISH)

        return try {
            LocalDateTime.parse(this, formatter1)
        } catch (e: Exception) {
            try {
                LocalDateTime.parse(this, formatter2)
            } catch (e: Exception) {
                LocalDateTime.parse(this, formatter3)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun LocalDateTime.toFormattedString(): String {
        val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm")
        return this.format(formatter)
    }
}