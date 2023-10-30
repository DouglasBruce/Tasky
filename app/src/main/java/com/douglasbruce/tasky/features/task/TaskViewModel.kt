package com.douglasbruce.tasky.features.task

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.common.utils.DateUtils
import com.douglasbruce.tasky.core.domain.repository.TaskRepository
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.model.NotificationType
import com.douglasbruce.tasky.features.task.form.TaskEvent
import com.douglasbruce.tasky.features.task.form.TaskState
import com.douglasbruce.tasky.features.task.navigation.TaskArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.UUID
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class TaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
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

            is TaskEvent.OnNotificationTypeSelection -> {
                state = state.copy(
                    notificationType = event.notificationType
                )
            }

            is TaskEvent.OnSaveClick -> {
                val task = AgendaItem.Task(
                    taskId = state.id ?: UUID.randomUUID().toString(),
                    taskTitle = state.title ?: "",
                    taskDescription = state.description,
                    time = ZonedDateTime.of(
                        state.date,
                        state.time,
                        ZonedDateTime.now().zone
                    ),
                    isDone = state.isDone,
                    remindAtTime = NotificationType.notificationTypeToZonedDateTime(
                        state.date,
                        state.time,
                        state.notificationType
                    ),
                    taskNotificationType = state.notificationType
                )

                viewModelScope.launch {
                    val result = when(state.isNew) {
                        true -> taskRepository.createTask(task)
                        false -> taskRepository.updateTask(task)
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