package com.douglasbruce.tasky.features.reminder

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.core.common.utils.DateUtils
import com.douglasbruce.tasky.features.reminder.form.ReminderEvent
import com.douglasbruce.tasky.features.reminder.form.ReminderState
import com.douglasbruce.tasky.features.reminder.navigation.ReminderArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalTime
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class ReminderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
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
        }
    }
}