package com.douglasbruce.tasky.features.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.common.utils.DateUtils
import com.douglasbruce.tasky.core.designsystem.component.AgendaDateTime
import com.douglasbruce.tasky.core.designsystem.component.AgendaDescription
import com.douglasbruce.tasky.core.designsystem.component.AgendaReminder
import com.douglasbruce.tasky.core.designsystem.component.AgendaTitle
import com.douglasbruce.tasky.core.designsystem.component.AgendaTypeIndicator
import com.douglasbruce.tasky.core.designsystem.component.TaskyCenterAlignedTopAppBar
import com.douglasbruce.tasky.core.designsystem.component.TaskyDatePicker
import com.douglasbruce.tasky.core.designsystem.component.TaskyTextButton
import com.douglasbruce.tasky.core.designsystem.component.TaskyTimePicker
import com.douglasbruce.tasky.core.designsystem.component.TaskyTopAppBarTextButton
import com.douglasbruce.tasky.core.designsystem.component.rememberAgendaReminderState
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.Green
import com.douglasbruce.tasky.core.designsystem.theme.LightBlue
import com.douglasbruce.tasky.core.designsystem.theme.TaskyTheme
import com.douglasbruce.tasky.core.designsystem.theme.White
import com.douglasbruce.tasky.features.task.form.TaskEvent
import com.douglasbruce.tasky.features.task.form.TaskState
import java.time.format.DateTimeFormatter

@Composable
internal fun TaskRoute(
    taskTitle: String,
    taskDescription: String,
    onBackClick: () -> Unit,
    onEditorClick: (Boolean, String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = hiltViewModel(),
) {
    LaunchedEffect(taskTitle, taskDescription) {
        viewModel.onEvent(
            TaskEvent.OnEditorSave(
                taskTitle,
                taskDescription
            )
        )
    }

    TaskScreen(
        onBackClick = onBackClick,
        onEditorClick = onEditorClick,
        taskUiState = viewModel.state,
        onEvent = viewModel::onEvent,
        modifier = modifier.fillMaxSize(),
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun TaskScreen(
    onBackClick: () -> Unit,
    onEditorClick: (Boolean, String, String) -> Unit,
    taskUiState: TaskState,
    onEvent: (TaskEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val titleDateFormatter = DateTimeFormatter.ofPattern("dd MMMM uuuu")
    val title = when {
        taskUiState.isNew -> stringResource(R.string.create_task)
        taskUiState.isEditing -> stringResource(R.string.edit_task)
        else -> taskUiState.date.format(titleDateFormatter)
    }

    Scaffold(
        topBar = {
            TaskyCenterAlignedTopAppBar(
                title = title.uppercase(),
                navigationIcon = TaskyIcons.Close,
                navigationIconContentDescription = stringResource(R.string.cancel),
                onNavigationClick = onBackClick,
                actions = when (taskUiState.isEditing) {
                    true -> {
                        {
                            TaskyTopAppBarTextButton(
                                text = stringResource(R.string.save),
                                onClick = { onEvent(TaskEvent.OnSaveClick) },
                                color = White,
                            )
                        }
                    }

                    else -> {
                        {
                            IconButton(onClick = { onEvent(TaskEvent.ToggleEditMode) }) {
                                Icon(
                                    imageVector = TaskyIcons.EditOutlined,
                                    contentDescription = stringResource(R.string.edit_task),
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = White,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = White
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.secondary,
        modifier = modifier,
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    )
                )
                .background(
                    color = MaterialTheme.colorScheme.primary,
                ),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
                    .padding(vertical = 16.dp),
            ) {
                val taskTitle =
                    if (taskUiState.title.isNullOrBlank()) stringResource(R.string.new_task) else taskUiState.title
                val taskDesc =
                    if (taskUiState.description.isNullOrBlank()) stringResource(R.string.task_description) else taskUiState.description
                val agendaReminderState = rememberAgendaReminderState(
                    initialSelectedNotificationType = taskUiState.notificationType
                )

                if (taskUiState.showTimePicker) {
                    val timePickerState = rememberTimePickerState(
                        initialHour = taskUiState.time.hour,
                        initialMinute = taskUiState.time.minute
                    )

                    TaskyTimePicker(
                        timePickerState = timePickerState,
                        onOkClick = {
                            onEvent(
                                TaskEvent.OnTimeSelected(
                                    timePickerState.hour,
                                    timePickerState.minute
                                )
                            )
                        },
                        onDismiss = { onEvent(TaskEvent.OnTimePickerClick(false)) },
                    )
                }

                if (taskUiState.showDatePicker) {
                    val datePickerState = rememberDatePickerState(
                        initialSelectedDateMillis = DateUtils.getDateMilli(taskUiState.date)
                    )

                    TaskyDatePicker(
                        datePickerState = datePickerState,
                        onOkClick = { onEvent(TaskEvent.OnDateSelected(datePickerState.selectedDateMillis!!)) },
                        onDismiss = { onEvent(TaskEvent.OnDatePickerClick(false)) },
                    )
                }

                AgendaTypeIndicator(
                    color = Green,
                    agendaType = stringResource(R.string.task),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                )
                AgendaTitle(
                    title = taskTitle,
                    isReadOnly = !taskUiState.isEditing,
                    onClick = { onEditorClick(true, "task_title", taskTitle) },
                )
                Divider(color = LightBlue)
                AgendaDescription(
                    description = taskDesc,
                    isReadOnly = !taskUiState.isEditing,
                    onClick = { onEditorClick(false, "task_desc", taskDesc) },
                )
                Divider(color = LightBlue)
                AgendaDateTime(
                    label = stringResource(R.string.at),
                    time = taskUiState.time,
                    date = taskUiState.date,
                    onTimeClick = { onEvent(TaskEvent.OnTimePickerClick(true)) },
                    onDateClick = { onEvent(TaskEvent.OnDatePickerClick(true)) },
                    isReadOnly = !taskUiState.isEditing,
                    modifier = Modifier.fillMaxWidth()
                )
                Divider(color = LightBlue)
                AgendaReminder(
                    agendaReminderState = agendaReminderState,
                    onSelectionClick = {
                        onEvent(
                            TaskEvent.OnNotificationTypeSelection(
                                agendaReminderState.selectedNotificationType.value
                            )
                        )
                    },
                    isReadOnly = !taskUiState.isEditing,
                )
                Divider(color = LightBlue)
                Spacer(Modifier.weight(1f))
                if (!taskUiState.isNew) {
                    TaskyTextButton(
                        text = stringResource(R.string.delete_task),
                        onClick = { /*TODO*/ },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskPreview() {
    TaskyTheme {
        TaskScreen(
            onBackClick = {},
            onEditorClick = { _: Boolean, _: String, _: String -> },
            taskUiState = TaskState(),
            onEvent = {},
        )
    }
}