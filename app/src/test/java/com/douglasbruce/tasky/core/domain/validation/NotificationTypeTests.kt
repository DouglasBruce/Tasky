package com.douglasbruce.tasky.core.domain.validation

import com.douglasbruce.tasky.core.model.NotificationType
import com.google.common.truth.Truth
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class NotificationTypeTests {
    @Test
    fun `dateTimeToNotificationType() remindAt is set 10 minutes before, TEN_MINUTES is returned`() {
        val time = ZonedDateTime.now()
        val remindAt = time.minusMinutes(10)
        val result = NotificationType.dateTimeToNotificationType(time, remindAt)
        Truth.assertThat(result).isEquivalentAccordingToCompareTo(NotificationType.TEN_MINUTES)
    }

    @Test
    fun `dateTimeToNotificationType() remindAt is set 30 minutes before, THIRTY_MINUTES is returned`() {
        val time = ZonedDateTime.now()
        val remindAt = time.minusMinutes(30)
        val result = NotificationType.dateTimeToNotificationType(time, remindAt)
        Truth.assertThat(result).isEquivalentAccordingToCompareTo(NotificationType.THIRTY_MINUTES)
    }

    @Test
    fun `dateTimeToNotificationType() remindAt is set 1 hour before, ONE_HOUR is returned`() {
        val time = ZonedDateTime.now()
        val remindAt = time.minusHours(1)
        val result = NotificationType.dateTimeToNotificationType(time, remindAt)
        Truth.assertThat(result).isEquivalentAccordingToCompareTo(NotificationType.ONE_HOUR)
    }

    @Test
    fun `dateTimeToNotificationType() remindAt is set 4 hours before, SIX_HOURS is returned`() {
        val time = ZonedDateTime.now()
        val remindAt = time.minusHours(4)
        val result = NotificationType.dateTimeToNotificationType(time, remindAt)
        Truth.assertThat(result).isEquivalentAccordingToCompareTo(NotificationType.SIX_HOURS)
    }

    @Test
    fun `dateTimeToNotificationType() remindAt is set 6 hours before, SIX_HOURS is returned`() {
        val time = ZonedDateTime.now()
        val remindAt = time.minusHours(6)
        val result = NotificationType.dateTimeToNotificationType(time, remindAt)
        Truth.assertThat(result).isEquivalentAccordingToCompareTo(NotificationType.SIX_HOURS)
    }

    @Test
    fun `dateTimeToNotificationType() remindAt is set 1 day before, ONE_DAY is returned`() {
        val time = ZonedDateTime.now()
        val remindAt = time.minusDays(1)
        val result = NotificationType.dateTimeToNotificationType(time, remindAt)
        Truth.assertThat(result).isEquivalentAccordingToCompareTo(NotificationType.ONE_DAY)
    }

    @Test
    fun `dateTimeToNotificationType() remindAt is set incorrectly 10 days before, THIRTY_MINUTES is returned`() {
        val time = ZonedDateTime.now()
        val remindAt = time.minusDays(10)
        val result = NotificationType.dateTimeToNotificationType(time, remindAt)
        Truth.assertThat(result).isEquivalentAccordingToCompareTo(NotificationType.THIRTY_MINUTES)
    }

    @Test
    fun `dateTimeToNotificationType() remindAt is set incorrectly 10 minutes after dateTime, THIRTY_MINUTES is returned`() {
        val time = ZonedDateTime.now()
        val remindAt = time.plusMinutes(10)
        val result = NotificationType.dateTimeToNotificationType(time, remindAt)
        Truth.assertThat(result).isEquivalentAccordingToCompareTo(NotificationType.THIRTY_MINUTES)
    }

    @Test
    fun `notificationTypeToZonedDateTime() notificationType set to TEN_MINUTES, correct dateTime return`() {
        val date = LocalDate.now()
        val time = LocalTime.now()
        val result = NotificationType.notificationTypeToZonedDateTime(
            date,
            time,
            NotificationType.TEN_MINUTES
        )
        Truth.assertThat(result).isEquivalentAccordingToCompareTo(
            ZonedDateTime.of(
                date,
                time.minusMinutes(10),
                ZoneId.systemDefault()
            )
        )
    }

    @Test
    fun `notificationTypeToZonedDateTime() notificationType set to THIRTY_MINUTES, correct dateTime return`() {
        val date = LocalDate.now()
        val time = LocalTime.now()
        val result = NotificationType.notificationTypeToZonedDateTime(
            date,
            time,
            NotificationType.THIRTY_MINUTES
        )
        Truth.assertThat(result).isEquivalentAccordingToCompareTo(
            ZonedDateTime.of(
                date,
                time.minusMinutes(30),
                ZoneId.systemDefault()
            )
        )
    }

    @Test
    fun `notificationTypeToZonedDateTime() notificationType set to ONE_HOUR, correct dateTime return`() {
        val date = LocalDate.now()
        val time = LocalTime.now()
        val result = NotificationType.notificationTypeToZonedDateTime(
            date,
            time,
            NotificationType.ONE_HOUR
        )
        Truth.assertThat(result).isEquivalentAccordingToCompareTo(
            ZonedDateTime.of(
                date,
                time.minusHours(1),
                ZoneId.systemDefault()
            )
        )
    }

    @Test
    fun `notificationTypeToZonedDateTime() notificationType set to SIX_HOURS, correct dateTime return`() {
        val date = LocalDate.now()
        val time = LocalTime.now()
        val result = NotificationType.notificationTypeToZonedDateTime(
            date,
            time,
            NotificationType.SIX_HOURS
        )
        Truth.assertThat(result).isEquivalentAccordingToCompareTo(
            ZonedDateTime.of(
                date,
                time.minusHours(6),
                ZoneId.systemDefault()
            )
        )
    }

    @Test
    fun `notificationTypeToZonedDateTime() notificationType set to ONE_DAY, correct dateTime return`() {
        val date = LocalDate.now()
        val time = LocalTime.now()
        val result = NotificationType.notificationTypeToZonedDateTime(
            date,
            time,
            NotificationType.ONE_DAY
        )
        Truth.assertThat(result).isEquivalentAccordingToCompareTo(
            ZonedDateTime.of(
                date.minusDays(1),
                time,
                ZoneId.systemDefault()
            )
        )
    }
}