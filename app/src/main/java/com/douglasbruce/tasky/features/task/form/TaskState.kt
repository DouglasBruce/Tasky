package com.douglasbruce.tasky.features.task.form

import android.os.Parcelable
import com.douglasbruce.tasky.core.model.NotificationType
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

@Parcelize
data class TaskState(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val isEditing: Boolean = id.isNullOrBlank(),
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now(),
    val isNew: Boolean = id.isNullOrBlank(),
    val notificationType: NotificationType = NotificationType.THIRTY_MINUTES,
    val isDone: Boolean = false,
    val showTimePicker: Boolean = false,
    val showDatePicker: Boolean = false,
    val isLoading: Boolean = false,
    val logout: Boolean = false,
    val saved: Boolean = false,
) : Parcelable