package com.douglasbruce.tasky.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@Parcelize
sealed class AgendaItem(
    val id: String,
    val title: String,
    val description: String?,
    val sortDate: ZonedDateTime,
    val remindAt: ZonedDateTime,
    val notificationType: NotificationType,
) : Parcelable {
    data class Event(
        val eventId: String,
        val eventTitle: String,
        val eventDescription: String?,
        val from: ZonedDateTime,
        val to: ZonedDateTime,
        val photos: List<AgendaPhoto>,
        val attendees: List<Attendee>,
        val isUserEventCreator: Boolean,
        val host: String?,
        val remindAtTime: ZonedDateTime,
        val eventNotificationType: NotificationType,
    ) : AgendaItem(
        eventId,
        eventTitle,
        eventDescription,
        from,
        remindAtTime,
        eventNotificationType
    ) {

        fun getAttendee(userId: String): Attendee? {
            return attendees.find { it.userId == userId }
        }

        companion object {
            const val MAX_PHOTO_AMOUNT = 10
        }
    }

    data class Task(
        val taskId: String,
        val taskTitle: String,
        val taskDescription: String?,
        val time: ZonedDateTime,
        val isDone: Boolean,
        val remindAtTime: ZonedDateTime,
        val taskNotificationType: NotificationType,
    ) : AgendaItem(taskId, taskTitle, taskDescription, time, remindAtTime, taskNotificationType)

    data class Reminder(
        val reminderId: String,
        val reminderTitle: String,
        val reminderDescription: String?,
        val time: ZonedDateTime,
        val remindAtTime: ZonedDateTime,
        val reminderNotificationType: NotificationType,
    ) : AgendaItem(
        reminderId,
        reminderTitle,
        reminderDescription,
        time,
        remindAtTime,
        reminderNotificationType
    )
}
