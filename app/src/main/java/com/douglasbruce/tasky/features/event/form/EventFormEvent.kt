package com.douglasbruce.tasky.features.event.form

import android.net.Uri
import com.douglasbruce.tasky.core.model.NotificationType
import com.douglasbruce.tasky.core.model.VisitorFilterType

sealed class EventFormEvent {
    data object ToggleEditMode : EventFormEvent()
    data class OnEditorSave(val title: String, val description: String) : EventFormEvent()
    data object OnHideTimePicker : EventFormEvent()
    data object OnHideDatePicker : EventFormEvent()
    data class OnTimePickerClick(val isToTime: Boolean) : EventFormEvent()
    data class OnTimeSelected(val hour: Int, val minute: Int) : EventFormEvent()
    data class OnDatePickerClick(val isToDate: Boolean) : EventFormEvent()
    data class OnDateSelected(val dateMillis: Long) : EventFormEvent()
    data class OnNotificationTypeSelection(val notificationType: NotificationType) : EventFormEvent()
    data class OnVisitorFilterTypeSelection(val visitorFilterType: VisitorFilterType) : EventFormEvent()
    data class OnAddPhotoClick(val uris: List<Uri>) : EventFormEvent()
    data class OnRemovePhotoClick(val location: String) : EventFormEvent()
}