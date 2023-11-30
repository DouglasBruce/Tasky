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
import com.douglasbruce.tasky.core.domain.repository.EventRepository
import com.douglasbruce.tasky.core.domain.repository.ReminderRepository
import com.douglasbruce.tasky.core.domain.repository.TaskRepository
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.features.agenda.form.AgendaEvent
import com.douglasbruce.tasky.features.agenda.form.AgendaState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class AgendaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    nameFormatter: NameFormatter,
    private val userDataPreferences: UserDataPreferences,
    private val agendaRepository: AgendaRepository,
    private val eventRepository: EventRepository,
    private val taskRepository: TaskRepository,
    private val reminderRepository: ReminderRepository,
) : ViewModel() {

    var state by savedStateHandle.saveable {
        mutableStateOf(AgendaState())
    }
        private set

    init {
        viewModelScope.launch {
            val selectedDateTime =
                ZonedDateTime.of(state.selectedDate.plusDays(state.selectedDay.toLong()), LocalTime.now(), ZoneId.systemDefault())
            agendaRepository.syncLocalDatabase(selectedDateTime, true)
        }

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

            is AgendaEvent.ToggleTaskDoneClick -> {
                val task = event.task.copy(isDone = !event.task.isDone)
                viewModelScope.launch {
                    taskRepository.updateTask(task)
                }
            }

            is AgendaEvent.OnDeleteEventClick -> {
                viewModelScope.launch {
                    when {
                        event.isUserEventCreator -> eventRepository.deleteEventById(event.eventId)
                        else -> eventRepository.leaveEvent(event.eventId)
                    }
                }
            }

            is AgendaEvent.OnDeleteTaskClick -> {
                viewModelScope.launch {
                    taskRepository.deleteTaskById(event.taskId)
                }
            }

            is AgendaEvent.OnDeleteReminderClick -> {
                viewModelScope.launch {
                    reminderRepository.deleteReminderById(event.reminderId)
                }
            }
        }
    }

    fun refreshAgenda() {
        viewModelScope.launch {
            val selectedDateTime =
                ZonedDateTime.of(state.selectedDate.plusDays(state.selectedDay.toLong()), LocalTime.now(), ZoneId.systemDefault())
            agendaRepository.syncLocalDatabase(selectedDateTime, true)
        }
    }

    private fun getAgendaForSelectedDate() {
        agendaRepository.getAgendaForDate(
            state.selectedDate.plusDays(state.selectedDay.toLong())
        ).onEach { agendaItems ->
            val item = getItemBeforeTimeNeedle(agendaItems)
            state = state.copy(
                items = agendaItems.sortedBy { it.sortDate },
                itemBeforeTimeNeedle = item
            )
        }.launchIn(viewModelScope)
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