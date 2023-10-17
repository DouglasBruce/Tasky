package com.douglasbruce.tasky.features.agenda.form

sealed class AgendaEvent {
    data object ToggleShowCreateAgendaOptions: AgendaEvent()
    data object ToggleShowSelectedAgendaOptions: AgendaEvent()
    data object ToggleShowAccountOptions: AgendaEvent()
}