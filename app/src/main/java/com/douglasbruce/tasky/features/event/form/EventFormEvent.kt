package com.douglasbruce.tasky.features.event.form

sealed class EventFormEvent {
    data object ToggleEditMode : EventFormEvent()
    data class OnEditorSave(val title: String, val description: String) : EventFormEvent()
    data object OnHideTimePicker : EventFormEvent()
    data object OnHideDatePicker : EventFormEvent()
    data class OnTimePickerClick(val isToTime: Boolean) : EventFormEvent()
    data class OnTimeSelected(val hour: Int, val minute: Int) : EventFormEvent()
    data class OnDatePickerClick(val isToDate: Boolean) : EventFormEvent()
    data class OnDateSelected(val dateMillis: Long) : EventFormEvent()
}