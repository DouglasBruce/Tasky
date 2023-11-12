package com.douglasbruce.tasky.features.event.form

import android.net.Uri
import com.douglasbruce.tasky.core.model.AttendeeFilterType
import com.douglasbruce.tasky.core.model.NotificationType

sealed class EventFormEvent {
    data object ToggleEditMode : EventFormEvent()
    data class OnEditorSave(val title: String, val description: String) : EventFormEvent()
    data object OnHideTimePicker : EventFormEvent()
    data object OnHideDatePicker : EventFormEvent()
    data class OnTimePickerClick(val isToTime: Boolean) : EventFormEvent()
    data class OnTimeSelected(val hour: Int, val minute: Int) : EventFormEvent()
    data class OnDatePickerClick(val isToDate: Boolean) : EventFormEvent()
    data class OnDateSelected(val dateMillis: Long) : EventFormEvent()
    data class OnNotificationTypeSelection(val notificationType: NotificationType) :
        EventFormEvent()

    data class OnVisitorFilterTypeSelection(val attendeeFilterType: AttendeeFilterType) :
        EventFormEvent()

    data class OnAddPhotoClick(val uris: List<Uri>) : EventFormEvent()
    data class OnRemovePhotoClick(val key: String) : EventFormEvent()
    data object OnSaveClick : EventFormEvent()
    data class ToggleDeleteConfirmationClick(val show: Boolean) : EventFormEvent()
    data object OnDeleteEventClick : EventFormEvent()
    data class ToggleAddVisitorDialogClick(val show: Boolean) : EventFormEvent()
    data class AttendeeEmailValueChanged(val email: String) : EventFormEvent()
    data object OnAddAttendeeClick : EventFormEvent()
    data class OnDeleteAttendeeClick(val userId: String) : EventFormEvent()
}