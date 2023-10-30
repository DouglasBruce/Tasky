package com.douglasbruce.tasky.core.model

import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.common.utils.UiText
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

enum class NotificationType(val text: UiText) {
    TEN_MINUTES(UiText.StringResource(R.string.ten_minutes_before)),
    THIRTY_MINUTES(UiText.StringResource(R.string.thirty_minutes_before)),
    ONE_HOUR(UiText.StringResource(R.string.one_hour_before)),
    SIX_HOURS(UiText.StringResource(R.string.six_hours_before)),
    ONE_DAY(UiText.StringResource(R.string.one_day_before));

    companion object {
        fun dateTimeToNotificationType(
            dateTime: ZonedDateTime,
            remindAt: ZonedDateTime,
        ): NotificationType {
            val truncatedDateTime = dateTime.truncatedTo(ChronoUnit.MINUTES)
            val truncatedRemindAt = remindAt.truncatedTo(ChronoUnit.MINUTES)

            if (truncatedRemindAt.isAfter(truncatedDateTime)) {
                return THIRTY_MINUTES
            }

            val delta = Duration.between(truncatedRemindAt, truncatedDateTime)

            return when {
                delta.seconds <= 600 -> TEN_MINUTES
                delta.seconds <= 1800 -> THIRTY_MINUTES
                delta.toHours() <= 1 -> ONE_HOUR
                delta.toHours() <= 6 -> SIX_HOURS
                delta.toDays() <= 1 -> ONE_DAY
                else -> THIRTY_MINUTES
            }
        }

        fun notificationTypeToZonedDateTime(
            date: LocalDate,
            time: LocalTime,
            notificationType: NotificationType,
        ): ZonedDateTime {
            val dateTime = ZonedDateTime.of(date, time, ZonedDateTime.now().zone)

            return when (notificationType) {
                TEN_MINUTES -> dateTime.minusMinutes(10)
                THIRTY_MINUTES -> dateTime.minusMinutes(30)
                ONE_HOUR -> dateTime.minusHours(1)
                SIX_HOURS -> dateTime.minusHours(6)
                ONE_DAY -> dateTime.minusDays(1)
            }
        }
    }
}