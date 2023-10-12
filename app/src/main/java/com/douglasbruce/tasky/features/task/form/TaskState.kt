package com.douglasbruce.tasky.features.task.form

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

@Parcelize
data class TaskState(
    val id: String = "-1",
    val title: String? = null,
    val description: String? = null,
    val isEditing: Boolean = true,
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now(),
    val isNew: Boolean = id == "-1",
) : Parcelable