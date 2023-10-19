package com.douglasbruce.tasky.features.event

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.features.event.form.EventFormEvent
import com.douglasbruce.tasky.features.event.form.EventState
import com.douglasbruce.tasky.features.event.navigation.EventArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class EventViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val eventArgs: EventArgs = EventArgs(savedStateHandle)

    var state by savedStateHandle.saveable {
        mutableStateOf(
            EventState(
                id = eventArgs.eventId,
                fromDate = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(eventArgs.eventFromDateMilli),
                    ZoneId.of("UTC")
                ).toLocalDate(),
                toDate = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(eventArgs.eventToDateMilli),
                    ZoneId.of("UTC")
                ).toLocalDate()
            )
        )
    }
        private set

    fun onEvent(event: EventFormEvent) {
        when (event) {
            is EventFormEvent.ToggleEditMode -> {
                state = state.copy(isEditing = !state.isEditing)
            }

            is EventFormEvent.OnEditorSave -> {
                state = state.copy(title = event.title, description = event.description)
            }
        }
    }
}