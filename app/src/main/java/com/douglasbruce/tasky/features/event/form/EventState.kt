package com.douglasbruce.tasky.features.event.form

import android.os.Parcelable
import com.douglasbruce.tasky.core.model.AgendaPhoto
import com.douglasbruce.tasky.core.model.NotificationType
import com.douglasbruce.tasky.core.model.VisitorFilterType
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

@Parcelize
data class EventState(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val isEditing: Boolean = id.isNullOrBlank(),
    val photos: List<AgendaPhoto> = emptyList(),
    val fromDate: LocalDate = LocalDate.now(),
    val fromTime: LocalTime = LocalTime.now(),
    val toDate: LocalDate = fromDate,
    val toTime: LocalTime = fromTime.plusMinutes(30),
    val isNew: Boolean = id.isNullOrBlank(),
    val notificationType: NotificationType = NotificationType.THIRTY_MINUTES,
    val visitorFilterType: VisitorFilterType = VisitorFilterType.ALL,
    val host: String? = null,
    val isUserEventCreator: Boolean = false,
    val showTimePicker: Boolean = false,
    val showDatePicker: Boolean = false,
    val isEditingToTime: Boolean = false,
    val isEditingToDate: Boolean = false,
    val isLoading: Boolean = false,
    val logout: Boolean = false,
    val closeScreen: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
) : Parcelable