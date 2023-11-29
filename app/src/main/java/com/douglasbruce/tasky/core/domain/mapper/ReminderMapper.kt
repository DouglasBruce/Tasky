package com.douglasbruce.tasky.core.domain.mapper

import com.douglasbruce.tasky.core.data.database.model.ReminderEntity
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.model.AlarmItem
import com.douglasbruce.tasky.core.model.NotificationType
import com.douglasbruce.tasky.core.network.model.NetworkReminder
import com.douglasbruce.tasky.core.network.model.request.CreateReminderRequest
import com.douglasbruce.tasky.core.network.model.request.UpdateReminderRequest
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

fun AgendaItem.Reminder.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = this.reminderId,
        title = this.reminderTitle,
        description = this.reminderDescription ?: "",
        time = this.time.toInstant().toEpochMilli(),
        remindAt = this.remindAtTime.toInstant().toEpochMilli(),
        notificationType = this.reminderNotificationType
    )
}

fun ReminderEntity.toReminder(): AgendaItem.Reminder {
    val time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.time), ZoneId.systemDefault())
    val remindAt =
        ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.remindAt), ZoneId.systemDefault())

    return AgendaItem.Reminder(
        reminderId = this.id,
        reminderTitle = this.title,
        reminderDescription = this.description,
        time = time,
        remindAtTime = remindAt,
        reminderNotificationType = this.notificationType
    )
}

fun NetworkReminder.toReminder(): AgendaItem.Reminder {
    val time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.time), ZoneId.systemDefault())
    val remindAt =
        ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.remindAt), ZoneId.systemDefault())

    return AgendaItem.Reminder(
        reminderId = this.id,
        reminderTitle = this.title,
        reminderDescription = this.description,
        time = time,
        remindAtTime = remindAt,
        reminderNotificationType = NotificationType.dateTimeToNotificationType(time, remindAt)
    )
}

fun AgendaItem.Reminder.toCreateReminderRequest(): CreateReminderRequest {
    return CreateReminderRequest(
        id = this.reminderId,
        title = this.reminderTitle,
        description = this.reminderDescription ?: "",
        time = this.time.toInstant().toEpochMilli(),
        remindAt = this.remindAtTime.toInstant().toEpochMilli(),
    )
}

fun AgendaItem.Reminder.toUpdateReminderRequest(): UpdateReminderRequest {
    return UpdateReminderRequest(
        id = this.reminderId,
        title = this.reminderTitle,
        description = this.reminderDescription ?: "",
        time = this.time.toInstant().toEpochMilli(),
        remindAt = this.remindAtTime.toInstant().toEpochMilli(),
    )
}

fun AgendaItem.Reminder.toAlarmItem(): AlarmItem {
    return AlarmItem(
        id = this.reminderId,
        title = this.reminderTitle,
        text = this.reminderDescription ?: "",
        time = this.remindAtTime.toLocalDateTime(),
        date = this.time.toInstant().toEpochMilli(),
        type = "Reminder",
    )
}