package com.douglasbruce.tasky.features.event

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.douglasbruce.tasky.core.designsystem.component.PhotoSelector
import com.douglasbruce.tasky.core.designsystem.component.TaskyCenterAlignedTopAppBar
import com.douglasbruce.tasky.core.designsystem.component.TaskyDatePicker
import com.douglasbruce.tasky.core.designsystem.component.TaskyTextButton
import com.douglasbruce.tasky.core.designsystem.component.TaskyTimePicker
import com.douglasbruce.tasky.core.designsystem.component.TaskyTopAppBarTextButton
import com.douglasbruce.tasky.core.designsystem.component.VisitorFilters
import com.douglasbruce.tasky.core.designsystem.component.rememberAgendaReminderState
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.Gray
import com.douglasbruce.tasky.core.designsystem.theme.LightBlue
import com.douglasbruce.tasky.core.designsystem.theme.LightBlueVariant
import com.douglasbruce.tasky.core.designsystem.theme.LightGreen
import com.douglasbruce.tasky.core.designsystem.theme.TaskyTheme
import com.douglasbruce.tasky.core.designsystem.theme.White
import com.douglasbruce.tasky.features.event.form.EventFormEvent
import com.douglasbruce.tasky.features.event.form.EventState
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
internal fun EventRoute(
    eventTitle: String,
    eventDescription: String,
    removePhotoLocation: String,
    onBackClick: () -> Unit,
    onEditorClick: (isTitle: Boolean, key: String, value: String) -> Unit,
    onPhotoViewerClick: (key: String, uri: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventViewModel = hiltViewModel(),
) {
    LaunchedEffect(eventTitle, eventDescription) {
        viewModel.onEvent(
            EventFormEvent.OnEditorSave(
                eventTitle,
                eventDescription
            )
        )
    }

    LaunchedEffect(removePhotoLocation) {
        viewModel.onEvent(EventFormEvent.OnRemovePhotoClick(removePhotoLocation))
    }

    EventScreen(
        onBackClick = onBackClick,
        onEditorClick = onEditorClick,
        onPhotoViewerClick = onPhotoViewerClick,
        eventUiState = viewModel.state,
        onEvent = viewModel::onEvent,
        modifier = modifier.fillMaxSize(),
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun EventScreen(
    onBackClick: () -> Unit,
    onEditorClick: (isTitle: Boolean, key: String, value: String) -> Unit,
    onPhotoViewerClick: (key: String, uri: String) -> Unit,
    eventUiState: EventState,
    onEvent: (EventFormEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val titleDateFormatter = DateTimeFormatter.ofPattern("dd MMMM uuuu")
    val title = when {
        eventUiState.isNew -> stringResource(R.string.create_event)
        eventUiState.isEditing -> stringResource(R.string.edit_event)
        else -> eventUiState.fromDate.format(titleDateFormatter)
    }

    Scaffold(
        topBar = {
            TaskyCenterAlignedTopAppBar(
                title = title.uppercase(),
                navigationIcon = TaskyIcons.Close,
                navigationIconContentDescription = stringResource(R.string.cancel),
                onNavigationClick = onBackClick,
                actions = when (eventUiState.isEditing) {
                    true -> {
                        {
                            TaskyTopAppBarTextButton(
                                text = stringResource(R.string.save),
                                onClick = { onEvent(EventFormEvent.OnSaveClick) },
                                color = White,
                            )
                        }
                    }

                    else -> {
                        {
                            IconButton(onClick = { onEvent(EventFormEvent.ToggleEditMode) }) {
                                Icon(
                                    imageVector = TaskyIcons.EditOutlined,
                                    contentDescription = stringResource(R.string.edit_event),
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
                val eventTitle =
                    if (eventUiState.title.isNullOrBlank()) stringResource(R.string.new_event) else eventUiState.title
                val eventDesc =
                    if (eventUiState.description.isNullOrBlank()) stringResource(R.string.event_description) else eventUiState.description
                val agendaReminderState = rememberAgendaReminderState(
                    initialSelectedNotificationType = eventUiState.notificationType
                )

                if (eventUiState.showTimePicker) {
                    val timePickerState = if (eventUiState.isEditingToTime) {
                        rememberTimePickerState(
                            initialHour = eventUiState.toTime.hour,
                            initialMinute = eventUiState.toTime.minute
                        )
                    } else {
                        rememberTimePickerState(
                            initialHour = eventUiState.fromTime.hour,
                            initialMinute = eventUiState.fromTime.minute
                        )
                    }

                    TaskyTimePicker(
                        timePickerState = timePickerState,
                        onOkClick = {
                            onEvent(
                                EventFormEvent.OnTimeSelected(
                                    timePickerState.hour,
                                    timePickerState.minute
                                )
                            )
                        },
                        onDismiss = { onEvent(EventFormEvent.OnHideTimePicker) },
                    )
                }

                if (eventUiState.showDatePicker) {
                    val datePickerState = if (eventUiState.isEditingToDate) {
                        rememberDatePickerState(
                            initialSelectedDateMillis = DateUtils.getDateMilli(
                                eventUiState.toDate,
                                ZoneId.of("UTC")
                            )
                        )
                    } else {
                        rememberDatePickerState(
                            initialSelectedDateMillis = DateUtils.getDateMilli(
                                eventUiState.fromDate,
                                ZoneId.of("UTC")
                            )
                        )
                    }

                    TaskyDatePicker(
                        datePickerState = datePickerState,
                        onOkClick = { onEvent(EventFormEvent.OnDateSelected(datePickerState.selectedDateMillis!!)) },
                        onDismiss = { onEvent(EventFormEvent.OnHideDatePicker) },
                    )
                }

                AgendaTypeIndicator(
                    color = LightGreen,
                    agendaType = stringResource(R.string.event),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                )
                AgendaTitle(
                    title = eventTitle,
                    isReadOnly = !eventUiState.isEditing,
                    onClick = { onEditorClick(true, "event_title", eventTitle) }
                )
                Divider(color = LightBlue)
                AgendaDescription(
                    description = eventDesc,
                    isReadOnly = !eventUiState.isEditing,
                    onClick = { onEditorClick(false, "event_desc", eventDesc) }
                )
                Spacer(Modifier.height(8.dp))
                PhotoSelector(
                    photos = eventUiState.photos,
                    onPhotoClick = { onPhotoViewerClick(it.key(), it.uri()) },
                    onPhotosSelected = {
                        onEvent(EventFormEvent.OnAddPhotoClick(it))
                    },
                )
                Spacer(Modifier.height(8.dp))
                Divider(color = LightBlue)
                AgendaDateTime(
                    label = stringResource(R.string.from),
                    time = eventUiState.fromTime,
                    date = eventUiState.fromDate,
                    onTimeClick = { onEvent(EventFormEvent.OnTimePickerClick(false)) },
                    onDateClick = { onEvent(EventFormEvent.OnDatePickerClick(false)) },
                    isReadOnly = !eventUiState.isEditing,
                    modifier = Modifier.fillMaxWidth()
                )
                Divider(color = LightBlue)
                AgendaDateTime(
                    label = stringResource(R.string.to),
                    time = eventUiState.toTime,
                    date = eventUiState.toDate,
                    onTimeClick = { onEvent(EventFormEvent.OnTimePickerClick(true)) },
                    onDateClick = { onEvent(EventFormEvent.OnDatePickerClick(true)) },
                    isReadOnly = !eventUiState.isEditing,
                    modifier = Modifier.fillMaxWidth()
                )
                Divider(color = LightBlue)
                AgendaReminder(
                    agendaReminderState = agendaReminderState,
                    onSelectionClick = {
                        onEvent(
                            EventFormEvent.OnNotificationTypeSelection(
                                agendaReminderState.selectedNotificationType.value
                            )
                        )
                    },
                    isReadOnly = !eventUiState.isEditing,
                )
                Divider(color = LightBlue)
                Spacer(Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(stringResource(R.string.visitors))
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .size(32.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(LightBlueVariant)
                            .clickable { /*TODO*/ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = TaskyIcons.Add,
                            contentDescription = stringResource(R.string.add_visitors),
                            tint = Gray,
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                VisitorFilters(
                    onClick = { onEvent(EventFormEvent.OnVisitorFilterTypeSelection(it)) },
                    selectedFilterType = eventUiState.visitorFilterType,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.weight(1f))
                if (!eventUiState.isNew) {
                    TaskyTextButton(
                        text = stringResource(R.string.delete_event).uppercase(),
                        onClick = { /*TODO*/ },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventPreview() {
    TaskyTheme {
        EventScreen(
            onBackClick = {},
            onEditorClick = { _: Boolean, _: String, _: String -> },
            onPhotoViewerClick = { _: String, _: String -> },
            eventUiState = EventState(),
            onEvent = {},
        )
    }
}