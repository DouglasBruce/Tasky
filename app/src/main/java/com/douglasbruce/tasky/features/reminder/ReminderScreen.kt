package com.douglasbruce.tasky.features.reminder

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
import com.douglasbruce.tasky.core.designsystem.theme.Gray
import com.douglasbruce.tasky.core.designsystem.theme.LightBlue
import com.douglasbruce.tasky.core.designsystem.theme.LightBlueVariant
import com.douglasbruce.tasky.core.designsystem.theme.TaskyTheme
import com.douglasbruce.tasky.core.designsystem.theme.White
import com.douglasbruce.tasky.features.reminder.form.ReminderEvent
import com.douglasbruce.tasky.features.reminder.form.ReminderState
import java.time.format.DateTimeFormatter

@Composable
internal fun ReminderRoute(
    reminderTitle: String,
    reminderDescription: String,
    onBackClick: () -> Unit,
    onEditorClick: (Boolean, String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReminderViewModel = hiltViewModel(),
) {
    LaunchedEffect(reminderTitle, reminderDescription) {
        viewModel.onEvent(
            ReminderEvent.OnEditorSave(
                reminderTitle,
                reminderDescription
            )
        )
    }

    ReminderScreen(
        onBackClick = onBackClick,
        onEditorClick = onEditorClick,
        reminderUiState = viewModel.state,
        onEvent = viewModel::onEvent,
        modifier = modifier.fillMaxSize(),
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun ReminderScreen(
    onBackClick: () -> Unit,
    onEditorClick: (Boolean, String, String) -> Unit,
    reminderUiState: ReminderState,
    onEvent: (ReminderEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val titleDateFormatter = DateTimeFormatter.ofPattern("dd MMMM uuuu")
    val title = when {
        reminderUiState.isNew -> stringResource(R.string.create_reminder)
        reminderUiState.isEditing -> stringResource(R.string.edit_reminder)
        else -> reminderUiState.date.format(titleDateFormatter)
    }

    Scaffold(
        topBar = {
            TaskyCenterAlignedTopAppBar(
                title = title.uppercase(),
                navigationIcon = TaskyIcons.Close,
                navigationIconContentDescription = stringResource(R.string.cancel),
                onNavigationClick = onBackClick,
                actions = when (reminderUiState.isEditing) {
                    true -> {
                        {
                            TaskyTopAppBarTextButton(
                                text = stringResource(R.string.save),
                                onClick = { /*TODO*/ },
                                color = White,
                            )
                        }
                    }

                    else -> {
                        {
                            IconButton(onClick = { onEvent(ReminderEvent.ToggleEditMode) }) {
                                Icon(
                                    imageVector = TaskyIcons.EditOutlined,
                                    contentDescription = stringResource(R.string.edit_reminder),
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
                val reminderTitle =
                    if (reminderUiState.title.isNullOrBlank()) stringResource(R.string.new_reminder) else reminderUiState.title
                val reminderDesc =
                    if (reminderUiState.description.isNullOrBlank()) stringResource(R.string.reminder_description) else reminderUiState.description
                val agendaReminderState = rememberAgendaReminderState(
                    initialSelectedNotificationType = reminderUiState.notificationType
                )

                if (reminderUiState.showTimePicker) {
                    val timePickerState = rememberTimePickerState(
                        initialHour = reminderUiState.time.hour,
                        initialMinute = reminderUiState.time.minute
                    )

                    TaskyTimePicker(
                        timePickerState = timePickerState,
                        onOkClick = {
                            onEvent(
                                ReminderEvent.OnTimeSelected(
                                    timePickerState.hour,
                                    timePickerState.minute
                                )
                            )
                        },
                        onDismiss = { onEvent(ReminderEvent.OnTimePickerClick(false)) },
                    )
                }

                if (reminderUiState.showDatePicker) {
                    val datePickerState = rememberDatePickerState(
                        initialSelectedDateMillis = DateUtils.getDateMilli(reminderUiState.date)
                    )

                    TaskyDatePicker(
                        datePickerState = datePickerState,
                        onOkClick = { onEvent(ReminderEvent.OnDateSelected(datePickerState.selectedDateMillis!!)) },
                        onDismiss = { onEvent(ReminderEvent.OnDatePickerClick(false)) },
                    )
                }

                AgendaTypeIndicator(
                    color = LightBlueVariant,
                    borderColor = Gray,
                    agendaType = stringResource(R.string.reminder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                )
                AgendaTitle(
                    title = reminderTitle,
                    isReadOnly = !reminderUiState.isEditing,
                    onClick = { onEditorClick(true, "reminder_title", reminderTitle) }
                )
                Divider(color = LightBlue)
                AgendaDescription(
                    description = reminderDesc,
                    isReadOnly = !reminderUiState.isEditing,
                    onClick = { onEditorClick(false, "reminder_desc", reminderDesc) }
                )
                Divider(color = LightBlue)
                AgendaDateTime(
                    label = stringResource(R.string.at),
                    time = reminderUiState.time,
                    date = reminderUiState.date,
                    onTimeClick = { onEvent(ReminderEvent.OnTimePickerClick(true)) },
                    onDateClick = { onEvent(ReminderEvent.OnDatePickerClick(true)) },
                    isReadOnly = !reminderUiState.isEditing,
                    modifier = Modifier.fillMaxWidth()
                )
                Divider(color = LightBlue)
                AgendaReminder(
                    agendaReminderState = agendaReminderState,
                    onSelectionClick = {
                        onEvent(
                            ReminderEvent.OnNotificationTypeSelection(
                                agendaReminderState.selectedNotificationType.value
                            )
                        )
                    },
                    isReadOnly = !reminderUiState.isEditing,
                )
                Divider(color = LightBlue)
                Spacer(Modifier.weight(1f))
                if (!reminderUiState.isNew) {
                    TaskyTextButton(
                        text = stringResource(R.string.delete_reminder),
                        onClick = { /*TODO*/ },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReminderPreview() {
    TaskyTheme {
        ReminderScreen(
            onBackClick = {},
            onEditorClick = { _: Boolean, _: String, _: String -> },
            reminderUiState = ReminderState(),
            onEvent = {},
        )
    }
}