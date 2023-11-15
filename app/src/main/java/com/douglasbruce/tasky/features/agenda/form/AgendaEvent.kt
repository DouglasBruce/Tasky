package com.douglasbruce.tasky.features.agenda.form

import com.douglasbruce.tasky.core.model.AgendaItem

sealed class AgendaEvent {
    data class OnCreateAgendaOptionsClick(val show: Boolean) : AgendaEvent()
    data class OnAgendaOptionsClick(val show: Boolean) : AgendaEvent()
    data class OnAccountOptionsClick(val show: Boolean) : AgendaEvent()
    data class OnDatePickerClick(val show: Boolean) : AgendaEvent()
    data class OnDateSelected(val dateMillis: Long) : AgendaEvent()
    data class OnDayClick(val day: Int) : AgendaEvent()
    data class ToggleTaskDoneClick(var task: AgendaItem.Task) : AgendaEvent()
    data class OnDeleteEventClick(val eventId: String, val isUserEventCreator: Boolean) : AgendaEvent()
    data class OnDeleteTaskClick(val taskId: String) : AgendaEvent()
    data class OnDeleteReminderClick(val reminderId: String) : AgendaEvent()
}