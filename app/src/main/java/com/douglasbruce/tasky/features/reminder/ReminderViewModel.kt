package com.douglasbruce.tasky.features.reminder

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.common.utils.DateUtils
import com.douglasbruce.tasky.core.domain.repository.ReminderRepository
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.model.NotificationType
import com.douglasbruce.tasky.features.reminder.form.ReminderEvent
import com.douglasbruce.tasky.features.reminder.form.ReminderState
import com.douglasbruce.tasky.features.reminder.navigation.ReminderArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.UUID
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class ReminderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    private val reminderArgs: ReminderArgs = ReminderArgs(savedStateHandle)

    var state by savedStateHandle.saveable {
        mutableStateOf(
            ReminderState(
                id = reminderArgs.reminderId,
                date = DateUtils.getLocalDate(reminderArgs.reminderDateMilli)
            )
        )
    }
        private set

    fun onEvent(event: ReminderEvent) {
        when (event) {
            is ReminderEvent.ToggleEditMode -> {
                state = state.copy(isEditing = !state.isEditing)
            }

            is ReminderEvent.OnEditorSave -> {
                state = state.copy(title = event.title, description = event.description)
            }

            is ReminderEvent.OnTimePickerClick -> {
                state = state.copy(showTimePicker = event.show)
            }

            is ReminderEvent.OnTimeSelected -> {
                val localTime: LocalTime = LocalTime.of(event.hour, event.minute)
                state = state.copy(
                    time = localTime,
                    showTimePicker = false,
                )
            }

            is ReminderEvent.OnDatePickerClick -> {
                state = state.copy(showDatePicker = event.show)
            }

            is ReminderEvent.OnDateSelected -> {
                val localDate = DateUtils.getLocalDate(event.dateMillis)

                state = state.copy(
                    date = localDate,
                    showDatePicker = false,
                )
            }

            is ReminderEvent.OnNotificationTypeSelection -> {
                state = state.copy(
                    notificationType = event.notificationType
                )
            }

            is ReminderEvent.OnSaveClick -> {
                val reminder = AgendaItem.Reminder(
                    reminderId = state.id ?: UUID.randomUUID().toString(),
                    reminderTitle = state.title ?: "",
                    reminderDescription = state.description,
                    time = ZonedDateTime.of(
                        state.date,
                        state.time,
                        ZonedDateTime.now().zone
                    ),
                    remindAtTime = NotificationType.notificationTypeToZonedDateTime(
                        state.date,
                        state.time,
                        state.notificationType
                    ),
                    reminderNotificationType = state.notificationType
                )

                viewModelScope.launch {
                    val result = when(state.isNew) {
                        true -> reminderRepository.createReminder(reminder)
                        false -> reminderRepository.updateReminder(reminder)
                    }

                    if (result is AuthResult.Success) {
                        //TODO: Navigate back to agenda screen
                    } else if (result is AuthResult.Error) {
                        //TODO: Report errors
                    }
                    //TODO: Logout if unauthorized
                }
            }
        }
    }
}