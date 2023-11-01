package com.douglasbruce.tasky.core.domain.mapper

import com.douglasbruce.tasky.core.data.database.model.TaskEntity
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.model.NotificationType
import com.douglasbruce.tasky.core.network.model.NetworkTask
import com.douglasbruce.tasky.core.network.model.request.CreateTaskRequest
import com.douglasbruce.tasky.core.network.model.request.UpdateTaskRequest
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

fun AgendaItem.Task.toTaskEntity(): TaskEntity {
    val utcTime = this.time.withZoneSameInstant(ZoneId.of("UTC"))
    val utcRemindAtTime = this.remindAt.withZoneSameInstant(ZoneId.of("UTC"))

    return TaskEntity(
        id = this.taskId,
        title = this.taskTitle,
        description = this.taskDescription ?: "",
        time = utcTime.toEpochSecond() * 1000,
        remindAt = utcRemindAtTime.toEpochSecond() * 1000,
        isDone = this.isDone,
        notificationType = this.taskNotificationType
    )
}

fun TaskEntity.toTask(): AgendaItem.Task {
    return AgendaItem.Task(
        taskId = this.id,
        taskTitle = this.title,
        taskDescription = this.description,
        time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.time), ZoneId.systemDefault()),
        isDone = this.isDone,
        remindAtTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.remindAt), ZoneId.systemDefault()),
        taskNotificationType = this.notificationType
    )
}

fun NetworkTask.toTask(): AgendaItem.Task {
    val time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.time), ZoneId.systemDefault())
    val remindAt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.remindAt), ZoneId.systemDefault())
    return AgendaItem.Task(
        taskId = this.id,
        taskTitle = this.title,
        taskDescription = this.description,
        time = time,
        isDone = this.isDone,
        remindAtTime = remindAt,
        taskNotificationType = NotificationType.dateTimeToNotificationType(time, remindAt)
    )
}

fun AgendaItem.Task.toCreateTaskRequest(): CreateTaskRequest {
    val utcTime = this.time.withZoneSameInstant(ZoneId.of("UTC"))
    val utcRemindAtTime = this.remindAt.withZoneSameInstant(ZoneId.of("UTC"))

    return CreateTaskRequest(
        id = this.taskId,
        title = this.taskTitle,
        description = this.taskDescription ?: "",
        time = utcTime.toEpochSecond() * 1000,
        remindAt = utcRemindAtTime.toEpochSecond() * 1000,
        isDone = this.isDone
    )
}

fun AgendaItem.Task.toUpdateTaskRequest(): UpdateTaskRequest {
    val utcTime = this.time.withZoneSameInstant(ZoneId.of("UTC"))
    val utcRemindAtTime = this.remindAt.withZoneSameInstant(ZoneId.of("UTC"))

    return UpdateTaskRequest(
        id = this.taskId,
        title = this.taskTitle,
        description = this.taskDescription ?: "",
        time = utcTime.toEpochSecond() * 1000,
        remindAt = utcRemindAtTime.toEpochSecond() * 1000,
        isDone = this.isDone
    )
}