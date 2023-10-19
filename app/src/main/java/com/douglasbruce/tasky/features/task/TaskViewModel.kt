package com.douglasbruce.tasky.features.task

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.features.task.form.TaskEvent
import com.douglasbruce.tasky.features.task.form.TaskState
import com.douglasbruce.tasky.features.task.navigation.TaskArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
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
                date = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(taskArgs.taskDateMilli),
                    ZoneId.of("UTC")
                ).toLocalDate()
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
        }
    }
}