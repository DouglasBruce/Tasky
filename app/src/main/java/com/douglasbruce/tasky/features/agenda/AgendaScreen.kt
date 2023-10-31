package com.douglasbruce.tasky.features.agenda

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.common.utils.DateUtils
import com.douglasbruce.tasky.core.designsystem.component.AgendaDayPicker
import com.douglasbruce.tasky.core.designsystem.component.AgendaEventCard
import com.douglasbruce.tasky.core.designsystem.component.AgendaReminderCard
import com.douglasbruce.tasky.core.designsystem.component.AgendaTaskCard
import com.douglasbruce.tasky.core.designsystem.component.TaskyDatePicker
import com.douglasbruce.tasky.core.designsystem.component.TaskyDropdownMenuItem
import com.douglasbruce.tasky.core.designsystem.component.TimeNeedle
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.Black
import com.douglasbruce.tasky.core.designsystem.theme.LightBlue
import com.douglasbruce.tasky.core.designsystem.theme.LightGrayBlue
import com.douglasbruce.tasky.core.designsystem.theme.TaskyTheme
import com.douglasbruce.tasky.core.designsystem.theme.White
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.features.agenda.form.AgendaEvent
import com.douglasbruce.tasky.features.agenda.form.AgendaState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
internal fun AgendaRoute(
    onLogoutClick: () -> Unit,
    onAddEventClick: (Long) -> Unit,
    onAddTaskClick: (Long) -> Unit,
    onAddReminderClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AgendaViewModel = hiltViewModel(),
) {
    AgendaScreen(
        agendaUiState = viewModel.state,
        onEvent = viewModel::onEvent,
        onLogoutClick = onLogoutClick,
        onAddEventClick = onAddEventClick,
        onAddTaskClick = onAddTaskClick,
        onAddReminderClick = onAddReminderClick,
        modifier = modifier.fillMaxSize(),
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun AgendaScreen(
    agendaUiState: AgendaState,
    onEvent: (AgendaEvent) -> Unit,
    onLogoutClick: () -> Unit,
    onAddEventClick: (Long) -> Unit,
    onAddTaskClick: (Long) -> Unit,
    onAddReminderClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextButton(onClick = { onEvent(AgendaEvent.OnDatePickerClick(true)) }) {
                        Text(
                            text = agendaUiState.selectedDate.month.name,
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 19.2.sp,
                                fontWeight = FontWeight(700),
                                color = White,
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = TaskyIcons.ArrowDropDown,
                            tint = White,
                            contentDescription = stringResource(R.string.change_date)
                        )
                    }
                },
                actions = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(LightBlue)
                            .clickable { onEvent(AgendaEvent.OnAccountOptionsClick(true)) },
                    ) {
                        Text(
                            text = agendaUiState.initials, style = TextStyle(
                                fontSize = 13.sp,
                                fontWeight = FontWeight(600),
                                color = LightGrayBlue,
                            )
                        )
                        DropdownMenu(
                            expanded = agendaUiState.showAccountOptions,
                            onDismissRequest = { onEvent(AgendaEvent.OnAccountOptionsClick(false)) },
                            offset = DpOffset(0.dp, 4.dp)
                        ) {
                            TaskyDropdownMenuItem(
                                text = stringResource(R.string.logout),
                                onClick = {
                                    onEvent(AgendaEvent.OnAccountOptionsClick(false))
                                    onLogoutClick()
                                },
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .wrapContentSize(Alignment.TopStart)
            ) {
                FloatingActionButton(
                    containerColor = Black,
                    contentColor = White,
                    onClick = { onEvent(AgendaEvent.OnCreateAgendaOptionsClick(true)) },
                ) {
                    Icon(imageVector = TaskyIcons.Add, contentDescription = null)
                }
                DropdownMenu(
                    expanded = agendaUiState.showCreateAgendaOptions,
                    onDismissRequest = { onEvent(AgendaEvent.OnCreateAgendaOptionsClick(false)) },
                    offset = DpOffset(0.dp, 4.dp)
                ) {
                    TaskyDropdownMenuItem(
                        text = stringResource(R.string.event),
                        onClick = {
                            onEvent(AgendaEvent.OnCreateAgendaOptionsClick(false))
                            onAddEventClick(DateUtils.getDateMilli(agendaUiState.displayDate))
                        },
                    )
                    TaskyDropdownMenuItem(
                        text = stringResource(R.string.task),
                        onClick = {
                            onEvent(AgendaEvent.OnCreateAgendaOptionsClick(false))
                            onAddTaskClick(DateUtils.getDateMilli(agendaUiState.displayDate))
                        },
                    )
                    TaskyDropdownMenuItem(
                        text = stringResource(R.string.reminder),
                        onClick = {
                            onEvent(AgendaEvent.OnCreateAgendaOptionsClick(false))
                            onAddReminderClick(DateUtils.getDateMilli(agendaUiState.displayDate))
                        },
                    )
                }
            }
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
                Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
                    .padding(12.dp), //TODO: See if removing bottom padding is a better design?
            ) {
                if (agendaUiState.showDatePicker) {
                    val datePickerState = rememberDatePickerState(
                        initialSelectedDateMillis = DateUtils.getDateMilli(agendaUiState.selectedDate)
                    )

                    TaskyDatePicker(
                        datePickerState = datePickerState,
                        onOkClick = { onEvent(AgendaEvent.OnDateSelected(datePickerState.selectedDateMillis!!)) },
                        onDismiss = { onEvent(AgendaEvent.OnDatePickerClick(false)) },
                    )
                }

                val today = rememberSaveable { LocalDate.now() }
                val yesterday = rememberSaveable { today.minusDays(1) }
                val tomorrow = rememberSaveable { today.plusDays(1) }
                val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM uuuu")

                AgendaDayPicker(
                    date = agendaUiState.selectedDate,
                    selectedDay = agendaUiState.selectedDay,
                    onClick = { onEvent(AgendaEvent.OnDayClick(it)) }
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = when (agendaUiState.displayDate) {
                        yesterday -> stringResource(R.string.yesterday)
                        today -> stringResource(R.string.today)
                        tomorrow -> stringResource(R.string.tomorrow)
                        else -> agendaUiState.displayDate.format(dateFormatter)
                    },
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight(700),
                        color = Black,
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                //TODO: Wrap in swipe to refresh or create refresh button
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        if (agendaUiState.itemBeforeTimeNeedle == null && agendaUiState.items.isNotEmpty()) {
                            TimeNeedle(modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    items(agendaUiState.items) { item ->
                        when (item) {
                            is AgendaItem.Event -> AgendaEventCard(
                                title = item.eventTitle.ifBlank { stringResource(R.string.new_event) },
                                content = if (item.eventDescription.isNullOrBlank()) stringResource(
                                    R.string.event_description
                                ) else item.eventDescription,
                                dateTime = DateUtils.formatDates(item.from, item.to),
                            )

                            is AgendaItem.Task -> AgendaTaskCard(
                                title = item.taskTitle.ifBlank { stringResource(R.string.new_task) },
                                content = if (item.taskDescription.isNullOrBlank()) stringResource(
                                    R.string.task_description
                                ) else item.taskDescription,
                                dateTime = DateUtils.formatDate(item.time),
                                isDone = item.isDone,
                                onLeadingIconClick = { /*TODO*/ },
                            )

                            is AgendaItem.Reminder -> AgendaReminderCard(
                                title = item.reminderTitle.ifBlank { stringResource(R.string.new_reminder) },
                                content = if (item.reminderDescription.isNullOrBlank()) stringResource(
                                    R.string.reminder_description
                                ) else item.reminderDescription,
                                dateTime = DateUtils.formatDate(item.time),
                            )
                        }
                        if (item == agendaUiState.itemBeforeTimeNeedle) {
                            Spacer(modifier = Modifier.height(4.dp))
                            TimeNeedle(modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(4.dp))
                        } else {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AgendaPreview() {
    TaskyTheme {
        AgendaScreen(
            agendaUiState = AgendaState(),
            onEvent = {},
            onLogoutClick = {},
            onAddEventClick = {},
            onAddTaskClick = {},
            onAddReminderClick = {},
        )
    }
}