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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.designsystem.component.TaskyDropdownMenuItem
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.Black
import com.douglasbruce.tasky.core.designsystem.theme.LightBlue
import com.douglasbruce.tasky.core.designsystem.theme.LightGrayBlue
import com.douglasbruce.tasky.core.designsystem.theme.TaskyTheme
import com.douglasbruce.tasky.core.designsystem.theme.White
import com.douglasbruce.tasky.features.agenda.form.AgendaEvent
import com.douglasbruce.tasky.features.agenda.form.AgendaState
import java.time.LocalDate

@Composable
internal fun AgendaRoute(
    onLogoutClick: () -> Unit,
    onAddEventClick: () -> Unit,
    onAddTaskClick: () -> Unit,
    onAddReminderClick: () -> Unit,
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
    onAddEventClick: () -> Unit,
    onAddTaskClick: () -> Unit,
    onAddReminderClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = agendaUiState.selectedDate.month.name)
                },
                actions = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(LightBlue)
                            .clickable { onEvent(AgendaEvent.ToggleShowAccountOptions) },
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
                            onDismissRequest = { onEvent(AgendaEvent.ToggleShowAccountOptions) },
                        ) {
                            TaskyDropdownMenuItem(
                                text = stringResource(R.string.logout),
                                onClick = {
                                    onEvent(AgendaEvent.ToggleShowAccountOptions)
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
                    onClick = { onEvent(AgendaEvent.ToggleShowCreateAgendaOptions) },
                ) {
                    Icon(imageVector = TaskyIcons.Add, contentDescription = null)
                }
                DropdownMenu(
                    expanded = agendaUiState.showCreateAgendaOptions,
                    onDismissRequest = { onEvent(AgendaEvent.ToggleShowCreateAgendaOptions) },
                ) {
                    TaskyDropdownMenuItem(
                        text = stringResource(R.string.event),
                        onClick = {
                            onEvent(AgendaEvent.ToggleShowCreateAgendaOptions)
                            onAddEventClick()
                        },
                    )
                    TaskyDropdownMenuItem(
                        text = stringResource(R.string.task),
                        onClick = {
                            onEvent(AgendaEvent.ToggleShowCreateAgendaOptions)
                            onAddTaskClick()
                        },
                    )
                    TaskyDropdownMenuItem(
                        text = stringResource(R.string.reminder),
                        onClick = {
                            onEvent(AgendaEvent.ToggleShowCreateAgendaOptions)
                            onAddReminderClick()
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
                    .padding(12.dp),
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = when (agendaUiState.selectedDate) {
                        LocalDate.now() -> stringResource(R.string.today)
                        LocalDate.now().plusDays(1) -> stringResource(R.string.tomorrow)
                        else -> agendaUiState.selectedDate.toString()
                    },
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight(700),
                        color = Black,
                    )
                )
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