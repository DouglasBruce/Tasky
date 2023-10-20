package com.douglasbruce.tasky.features.task

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.core.common.utils.DateUtils
import com.douglasbruce.tasky.features.task.form.TaskEvent
import com.douglasbruce.tasky.features.task.form.TaskState
import com.douglasbruce.tasky.features.task.navigation.TaskArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalTime
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class TaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val taskArgs: TaskArgs = TaskArgs(savedStateHandle)

    var state by savedStateHandle.saveable {
        mutableStateOf(
            TaskState(
                id = taskArgs.taskId,
                date = DateUtils.getLocalDate(taskArgs.taskDateMilli)
            )
        )
    }
        private set

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.ToggleEditMode -> {
                state = state.copy(isEditing = !state.isEditing)
            }

            is TaskEvent.OnEditorSave -> {
                state = state.copy(title = event.title, description = event.description)
            }

            is TaskEvent.OnTimePickerClick -> {
                state = state.copy(showTimePicker = event.show)
            }

            is TaskEvent.OnTimeSelected -> {
                val localTime: LocalTime = LocalTime.of(event.hour, event.minute)
                state = state.copy(
                    time = localTime,
                    showTimePicker = false,
                )
            }

            is TaskEvent.OnDatePickerClick -> {
                state = state.copy(showDatePicker = event.show)
            }

            is TaskEvent.OnDateSelected -> {
                val localDate = DateUtils.getLocalDate(event.dateMillis)

                state = state.copy(
                    date = localDate,
                    showDatePicker = false,
                )
            }
        }
    }
}