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
    val toDate: LocalDate = fromDate,
    val toTime: LocalTime = fromTime.plusMinutes(30),
    val isNew: Boolean = id.isNullOrBlank(),
    val showTimePicker: Boolean = false,
    val showDatePicker: Boolean = false,
    val isEditingToTime: Boolean = false,
    val isEditingToDate: Boolean = false,
) : Parcelable