package com.douglasbruce.tasky.features.reminder.form

import com.douglasbruce.tasky.core.model.NotificationType

sealed class ReminderEvent {
    data object ToggleEditMode : ReminderEvent()
    data class OnEditorSave(val title: String, val description: String) : ReminderEvent()
    data class OnTimePickerClick(val show: Boolean) : ReminderEvent()
    data class OnTimeSelected(val hour: Int, val minute: Int) : ReminderEvent()
    data class OnDatePickerClick(val show: Boolean) : ReminderEvent()
    data class OnDateSelected(val dateMillis: Long) : ReminderEvent()
    data class OnNotificationTypeSelection(val notificationType: NotificationType) : ReminderEvent()
}