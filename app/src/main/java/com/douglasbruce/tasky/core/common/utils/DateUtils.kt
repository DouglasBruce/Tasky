package com.douglasbruce.tasky.core.common.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

sealed class DateUtils {

    companion object {
        fun getDateMilli(date: LocalDate): Long {
            return date.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()
        }

        fun getLocalDate(dateMilli: Long): LocalDate {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(dateMilli), ZoneId.of("UTC"))
                .toLocalDate()
        }
    }
}