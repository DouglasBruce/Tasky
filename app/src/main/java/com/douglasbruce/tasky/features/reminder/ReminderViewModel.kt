package com.douglasbruce.tasky.features.reminder

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.features.reminder.form.ReminderEvent
import com.douglasbruce.tasky.features.reminder.form.ReminderState
import com.douglasbruce.tasky.features.reminder.navigation.ReminderArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
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
                date = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(reminderArgs.reminderDateMilli),
                    ZoneId.of("UTC")
                ).toLocalDate()
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
        }
    }
}