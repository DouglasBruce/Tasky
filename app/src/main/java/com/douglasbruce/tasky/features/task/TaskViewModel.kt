package com.douglasbruce.tasky.features.task

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.common.utils.DateUtils
import com.douglasbruce.tasky.core.common.utils.UiText
import com.douglasbruce.tasky.core.domain.repository.TaskRepository
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.model.NotificationType
import com.douglasbruce.tasky.features.task.form.TaskEvent
import com.douglasbruce.tasky.features.task.form.TaskState
import com.douglasbruce.tasky.features.task.navigation.TaskArgs
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
class TaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
) : ViewModel() {

    private val taskArgs: TaskArgs = TaskArgs(savedStateHandle)

    private val infoChannel = Channel<UiText>()
    val infoMessages = infoChannel.receiveAsFlow()

    var state by savedStateHandle.saveable {
        mutableStateOf(
            TaskState(
                id = taskArgs.taskId,
                date = DateUtils.getLocalDate(taskArgs.taskDateMilli),
                isEditing = taskArgs.taskIsEditing
            )
        )
    }
        private set

    init {
        state.id?.let { id ->
            getTask(id)
        }
    }

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.ToggleEditMode -> {
                state = state.copy(isEditing = !state.isEditing)
            }

            is TaskEvent.OnEditorSave -> {
                if (state.isLoading) {
                    return
                }

                val title = event.title.ifBlank { state.title }
                val desc = event.description.ifBlank { state.description }

                state = state.copy(title = title, description = desc)
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
                    taskTitle = state.title ?: event.defaultTitle,
                    taskDescription = state.description ?: event.defaultDesc,
                    time = ZonedDateTime.of(
                        state.date,
                        state.time,
                        ZoneId.systemDefault()
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
                    val result = when (state.isNew) {
                        true -> taskRepository.createTask(task)
                        false -> taskRepository.updateTask(task)
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

            is TaskEvent.ToggleDeleteConfirmationClick -> {
                state = state.copy(showDeleteConfirmation = event.show)
            }

            is TaskEvent.OnDeleteTaskClick -> {
                state.id?.let { id ->
                    viewModelScope.launch {
                        when (taskRepository.deleteTaskById(id)) {
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

    private fun getTask(id: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            when (val result = taskRepository.getTaskById(id)) {
                is AuthResult.Success -> {
                    result.data?.let { task ->
                        state = state.copy(
                            title = task.taskTitle,
                            description = task.taskDescription,
                            time = task.time.toLocalTime(),
                            date = task.time.toLocalDate(),
                            isDone = task.isDone,
                            notificationType = task.taskNotificationType,
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