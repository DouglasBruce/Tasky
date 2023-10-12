package com.douglasbruce.tasky.features.event.form

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

@Parcelize
data class EventState(
    val id: String = "-1",
    val title: String? = null,
    val description: String? = null,
    val isEditing: Boolean = true,
    val fromDate: LocalDate = LocalDate.now(),
    val fromTime: LocalTime = LocalTime.now(),
    val toDate: LocalDate = LocalDate.now(),
    val toTime: LocalTime = LocalTime.now(),
    val isNew: Boolean = id == "-1",
) : Parcelable