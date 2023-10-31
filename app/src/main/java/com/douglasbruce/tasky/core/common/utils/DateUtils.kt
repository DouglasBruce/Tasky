package com.douglasbruce.tasky.core.common.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateUtils {
    fun getDateMilli(date: LocalDate): Long {
        return date.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()
    }

    fun getLocalDate(dateMilli: Long): LocalDate {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(dateMilli), ZoneId.of("UTC"))
            .toLocalDate()
    }

    fun formatDate(date: ZonedDateTime): String {
        val dateFormatter = DateTimeFormatter.ofPattern("MMM d, HH:mm")
        return date.format(dateFormatter)
    }

    fun formatDates(fromDate: ZonedDateTime, toDate: ZonedDateTime): String {
        val dateFormatter = DateTimeFormatter.ofPattern("MMM d, HH:mm")
        return "${fromDate.format(dateFormatter)} - ${toDate.format(dateFormatter)}"
    }
}