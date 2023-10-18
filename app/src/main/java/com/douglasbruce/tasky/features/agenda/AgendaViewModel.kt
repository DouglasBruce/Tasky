package com.douglasbruce.tasky.features.agenda

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.core.domain.datastore.UserDataPreferences
import com.douglasbruce.tasky.core.domain.formatter.NameFormatter
import com.douglasbruce.tasky.features.agenda.form.AgendaEvent
import com.douglasbruce.tasky.features.agenda.form.AgendaState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class AgendaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userDataPreferences: UserDataPreferences,
    private val nameFormatter: NameFormatter,
) : ViewModel() {

    var state by savedStateHandle.saveable {
        mutableStateOf(AgendaState())
    }
        private set

    init {
        val fullName = runBlocking {
            userDataPreferences.userData.map { it.fullName }.first()
        }

        state = state.copy(initials = nameFormatter.getInitials(fullName))
    }

    fun onEvent(event: AgendaEvent) {
        when (event) {
            is AgendaEvent.OnCreateAgendaOptionsClick -> {
                state = state.copy(showCreateAgendaOptions = event.show)
            }

            is AgendaEvent.OnAgendaOptionsClick -> {
                state = state.copy(showSelectedAgendaOptions = event.show)
            }

            is AgendaEvent.OnAccountOptionsClick -> {
                state = state.copy(showAccountOptions = event.show)
            }

            is AgendaEvent.OnDatePickerClick -> {
                state = state.copy(showDatePicker = event.show)
            }

            is AgendaEvent.OnDateSelected -> {
                val localDate = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(event.dateMillis),
                    ZoneId.of("UTC")
                ).toLocalDate()

                state = state.copy(
                    selectedDate = localDate,
                    displayDate = localDate,
                    selectedDay = 0,
                    showDatePicker = false,
                )
            }

            is AgendaEvent.OnDayClick -> {
                state = state.copy(
                    selectedDay = event.day,
                    displayDate = state.selectedDate.plusDays(event.day.toLong())
                )
            }
        }
    }
}