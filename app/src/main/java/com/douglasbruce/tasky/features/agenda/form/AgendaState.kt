package com.douglasbruce.tasky.features.agenda.form

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class AgendaState(
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedDay: Int = 0,
    val displayDate: LocalDate = selectedDate.plusDays(selectedDay.toLong()),
    val initials: String = "",
    val agendaItems: List<String> = emptyList(),
    val showCreateAgendaOptions: Boolean = false,
    val showSelectedAgendaOptions: Boolean = false,
    val showAccountOptions: Boolean = false,
    val showDatePicker: Boolean = false,
) : Parcelable