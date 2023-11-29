package com.douglasbruce.tasky.features.reminder

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.common.utils.DateUtils
import com.douglasbruce.tasky.core.common.utils.UiText
import com.douglasbruce.tasky.core.domain.repository.ReminderRepository
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.model.NotificationType
import com.douglasbruce.tasky.features.reminder.form.ReminderEvent
import com.douglasbruce.tasky.features.reminder.form.ReminderState
import com.douglasbruce.tasky.features.reminder.navigation.ReminderArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class ReminderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reminderRepository: ReminderRepository,
) : ViewModel() {

    private val reminderArgs: ReminderArgs = ReminderArgs(savedStateHandle)

    private val infoChannel = Channel<UiText>()
    val infoMessages = infoChannel.receiveAsFlow()

    var state by savedStateHandle.saveable {
        mutableStateOf(
            ReminderState(
                id = reminderArgs.reminderId,
                date = DateUtils.getLocalDate(reminderArgs.reminderDateMilli),
                isEditing = reminderArgs.reminderIsEditing,
            )
        )
    }
        private set

    init {
        state.id?.let { id ->
            getReminder(id)
        }
    }

    fun onEvent(event: ReminderEvent) {
        when (event) {
            is ReminderEvent.ToggleEditMode -> {
                state = state.copy(isEditing = !state.isEditing)
            }

            is ReminderEvent.OnEditorSave -> {
                if (state.isLoading) {
                    return
                }

                val title = event.title.ifBlank { state.title }
                val desc = event.description.ifBlank { state.description }

                state = state.copy(title = title, description = desc)
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
                    reminderTitle = state.title ?: event.defaultTitle,
                    reminderDescription = state.description ?: event.defaultDesc,
                    time = ZonedDateTime.of(
                        state.date,
                        state.time,
                        ZoneId.systemDefault()
                    ),
                    remindAtTime = NotificationType.notificationTypeToZonedDateTime(
                        state.date,
                        state.time,
                        state.notificationType
                    ),
                    reminderNotificationType = state.notificationType
                )

                viewModelScope.launch {
                    val result = when (state.isNew) {
                        true -> reminderRepository.createReminder(reminder)
                        false -> reminderRepository.updateReminder(reminder)
                    }

                    when (result) {
                        is AuthResult.Success -> {
                            state = state.copy(closeScreen = true)
                        }

                        is AuthResult.Error -> {
                            state = state.copy(closeScreen = true)
                        }

                        else -> {}
                    }
                }
            }

            is ReminderEvent.ToggleDeleteConfirmationClick -> {
                state = state.copy(showDeleteConfirmation = event.show)
            }

            is ReminderEvent.OnDeleteReminderClick -> {
                state.id?.let { id ->
                    viewModelScope.launch {
                        when (reminderRepository.deleteReminderById(id)) {
                            is AuthResult.Success -> {
                                state = state.copy(closeScreen = true)
                            }

                            is AuthResult.Error -> {
                                state = state.copy(closeScreen = true)
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun getReminder(id: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            when (val result = reminderRepository.getReminderById(id)) {
                is AuthResult.Success -> {
                    result.data?.let { reminder ->
                        state = state.copy(
                            title = reminder.reminderTitle,
                            description = reminder.reminderDescription,
                            time = reminder.time.toLocalTime(),
                            date = reminder.time.toLocalDate(),
                            notificationType = reminder.reminderNotificationType,
                            isLoading = false,
                        )
                    }
                }

                is AuthResult.Error -> {
                    result.message?.let {
                        infoChannel.send(result.message)
                    }
                    state = state.copy(isLoading = false)
                }

                else -> {}
            }
        }
    }
}