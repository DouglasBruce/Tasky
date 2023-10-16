package com.douglasbruce.tasky.features.event.form

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

@Parcelize
data class EventState(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val isEditing: Boolean = id.isNullOrBlank(),
    val fromDate: LocalDate = LocalDate.now(),
    val fromTime: LocalTime = LocalTime.now(),
    val toDate: LocalDate = LocalDate.now(),
    val toTime: LocalTime = LocalTime.now(),
    val isNew: Boolean = id.isNullOrBlank(),
) : Parcelable