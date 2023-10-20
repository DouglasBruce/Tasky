package com.douglasbruce.tasky.features.event

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.core.common.utils.DateUtils
import com.douglasbruce.tasky.features.event.form.EventFormEvent
import com.douglasbruce.tasky.features.event.form.EventState
import com.douglasbruce.tasky.features.event.navigation.EventArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalTime
import java.time.temporal.ChronoUnit
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
                fromDate = DateUtils.getLocalDate(eventArgs.eventFromDateMilli),
                toDate = DateUtils.getLocalDate(eventArgs.eventToDateMilli),
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

            is EventFormEvent.OnHideTimePicker -> {
                state = state.copy(showTimePicker = false)
            }

            is EventFormEvent.OnHideDatePicker -> {
                state = state.copy(showDatePicker = false)
            }

            is EventFormEvent.OnTimePickerClick -> {
                state = state.copy(
                    isEditingToTime = event.isToTime,
                    showTimePicker = true,
                )
            }

            is EventFormEvent.OnTimeSelected -> {
                val localTime: LocalTime = LocalTime.of(event.hour, event.minute)

                state = if (state.isEditingToTime) {
                    state.copy(
                        toTime = localTime,
                        showTimePicker = false,
                    )
                } else {
                    val truncatedLocalTime = localTime.truncatedTo(ChronoUnit.MINUTES)
                    val truncatedToTime = state.toTime.truncatedTo(ChronoUnit.MINUTES)

                    val toTime = if (truncatedLocalTime == truncatedToTime || truncatedLocalTime.isAfter(truncatedToTime)) {
                        localTime.plusMinutes(30)
                    } else {
                        state.toTime
                    }

                    state.copy(
                        fromTime = localTime,
                        toTime = toTime,
                        showTimePicker = false,
                    )
                }
            }

            is EventFormEvent.OnDatePickerClick -> {
                state = state.copy(
                    isEditingToDate = event.isToDate,
                    showDatePicker = true,
                )
            }

            is EventFormEvent.OnDateSelected -> {
                val localDate = DateUtils.getLocalDate(event.dateMillis)

                state = if (state.isEditingToDate) {
                    state.copy(
                        toDate = localDate,
                        showDatePicker = false,
                    )
                } else {
                    val toDate = if (localDate.isAfter(state.toDate)) {
                        localDate
                    } else {
                        state.toDate
                    }

                    state.copy(
                        fromDate = localDate,
                        toDate = toDate,
                        showDatePicker = false,
                    )
                }
            }
        }
    }
}