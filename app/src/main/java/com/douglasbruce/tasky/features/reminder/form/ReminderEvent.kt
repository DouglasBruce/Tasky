package com.douglasbruce.tasky.features.reminder.form

sealed class ReminderEvent {
    data object ToggleEditMode : ReminderEvent()
    data class OnEditorSave(val title: String, val description: String) : ReminderEvent()
}