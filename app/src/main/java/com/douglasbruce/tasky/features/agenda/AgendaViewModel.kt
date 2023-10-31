package com.douglasbruce.tasky.features.agenda

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.core.common.utils.DateUtils
import com.douglasbruce.tasky.core.domain.datastore.UserDataPreferences
import com.douglasbruce.tasky.core.domain.formatter.NameFormatter
import com.douglasbruce.tasky.core.domain.repository.AgendaRepository
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.features.agenda.form.AgendaEvent
import com.douglasbruce.tasky.features.agenda.form.AgendaState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalTime
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class AgendaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    nameFormatter: NameFormatter,
    private val userDataPreferences: UserDataPreferences,
    private val agendaRepository: AgendaRepository
) : ViewModel() {

    var state by savedStateHandle.saveable {
        mutableStateOf(AgendaState())
    }
        private set

    init {
        val fullName = runBlocking {
            userDataPreferences.userData.map { it.fullName }.first()
        }

        getAgendaForSelectedDate()

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
                val localDate = DateUtils.getLocalDate(event.dateMillis)

                state = state.copy(
                    selectedDate = localDate,
                    displayDate = localDate,
                    selectedDay = 0,
                    showDatePicker = false,
                )
                getAgendaForSelectedDate()
            }

            is AgendaEvent.OnDayClick -> {
                state = state.copy(
                    selectedDay = event.day,
                    displayDate = state.selectedDate.plusDays(event.day.toLong())
                )
                getAgendaForSelectedDate()
            }
        }
    }

    private fun getAgendaForSelectedDate() {
        viewModelScope.launch {
            agendaRepository.getAgendaForDate(
                state.selectedDate.plusDays(state.selectedDay.toLong())
            ).collect { agendaItems ->
                val item = getItemBeforeTimeNeedle(agendaItems)
                state = state.copy(
                    items = agendaItems.sortedBy { it.sortDate },
                    itemBeforeTimeNeedle = item
                )
            }
        }
    }

    private fun getItemBeforeTimeNeedle(
        items: List<AgendaItem>,
        time: LocalTime = LocalTime.now(),
    ): AgendaItem? {
        if (items.size == 1) {
            return if (items.first().sortDate.toLocalTime() < time) items.first() else null
        }

        val isNeedleAtEnd = items.all { it.sortDate.toLocalTime() < time }
        if (isNeedleAtEnd) {
            return items.lastOrNull()
        }

        for (i in (0..items.size - 2)) {
            val currentItemTime = items[i].sortDate.toLocalTime()
            val nextItemTime = items[i + 1].sortDate.toLocalTime()
            if (time in currentItemTime..nextItemTime) {
                return items[i]
            }
        }
        return null
    }
}