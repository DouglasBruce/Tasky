package com.douglasbruce.tasky.features.reminder.form

sealed class ReminderEvent {
    data object ToggleEditMode : ReminderEvent()
    data class OnEditorSave(val title: String, val description: String) : ReminderEvent()
    data class OnTimePickerClick(val show: Boolean) : ReminderEvent()
    data class OnTimeSelected(val hour: Int, val minute: Int) : ReminderEvent()
    data class OnDatePickerClick(val show: Boolean) : ReminderEvent()
    data class OnDateSelected(val dateMillis: Long) : ReminderEvent()
}