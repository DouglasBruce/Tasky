package com.douglasbruce.tasky.features.agenda.form

sealed class AgendaEvent {
    data class OnCreateAgendaOptionsClick(val show: Boolean) : AgendaEvent()
    data class OnAgendaOptionsClick(val show: Boolean) : AgendaEvent()
    data class OnAccountOptionsClick(val show: Boolean) : AgendaEvent()
    data class OnDatePickerClick(val show: Boolean) : AgendaEvent()
    data class OnDateSelected(val dateMillis: Long) : AgendaEvent()
    data class OnDayClick(val day: Int) : AgendaEvent()
}