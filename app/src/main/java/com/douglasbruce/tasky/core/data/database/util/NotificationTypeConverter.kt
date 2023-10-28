package com.douglasbruce.tasky.core.data.database.util

import androidx.room.TypeConverter
import com.douglasbruce.tasky.core.model.NotificationType

class NotificationTypeConverter {
    @TypeConverter
    fun notificationTypeToString(notificationType: NotificationType): String =
        notificationType.name

    @TypeConverter
    fun stringToNotificationType(string: String): NotificationType =
        NotificationType.valueOf(string)
}