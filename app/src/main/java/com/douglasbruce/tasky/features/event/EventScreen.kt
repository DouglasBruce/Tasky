package com.douglasbruce.tasky.features.event

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.designsystem.component.AgendaDateTime
import com.douglasbruce.tasky.core.designsystem.component.AgendaDescription
import com.douglasbruce.tasky.core.designsystem.component.AgendaReminder
import com.douglasbruce.tasky.core.designsystem.component.AgendaTitle
import com.douglasbruce.tasky.core.designsystem.component.AgendaTypeIndicator
import com.douglasbruce.tasky.core.designsystem.component.TaskyCenterAlignedTopAppBar
import com.douglasbruce.tasky.core.designsystem.component.TaskyTextButton
import com.douglasbruce.tasky.core.designsystem.component.TaskyTopAppBarTextButton
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.Gray
import com.douglasbruce.tasky.core.designsystem.theme.LightBlue
import com.douglasbruce.tasky.core.designsystem.theme.LightBlueVariant
import com.douglasbruce.tasky.core.designsystem.theme.LightGreen
import com.douglasbruce.tasky.core.designsystem.theme.TaskyTheme
import com.douglasbruce.tasky.core.designsystem.theme.White
import com.douglasbruce.tasky.features.event.form.EventState
import java.time.format.DateTimeFormatter

@Composable
internal fun EventRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventViewModel = hiltViewModel(),
) {
    EventScreen(
        onBackClick = onBackClick,
        eventUiState = viewModel.state,
        modifier = modifier.fillMaxSize(),
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun EventScreen(
    onBackClick: () -> Unit,
    eventUiState: EventState,
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
                                onClick = { /*TODO*/ },
                                color = White,
                            )
                        }
                    }
                    else -> {
                        {
                            IconButton(onClick = { /*TODO*/ }) {
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
                AgendaTypeIndicator(
                    color = LightGreen,
                    agendaType = stringResource(R.string.event),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                )
                AgendaTitle(
                    title = if (eventUiState.title.isNullOrBlank()) stringResource(R.string.new_event) else eventUiState.title,
                    isReadOnly = !eventUiState.isEditing,
                    onClick = { /*TODO*/ }
                )
                Divider(color = LightBlue)
                AgendaDescription(
                    description = if (eventUiState.description.isNullOrBlank()) stringResource(R.string.event_description) else eventUiState.description,
                    isReadOnly = !eventUiState.isEditing,
                    onClick = { /*TODO*/ }
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(color = LightBlueVariant)
                        .clickable { }
                ) {
                    Icon(
                        imageVector = TaskyIcons.Add,
                        contentDescription = null,
                        tint = Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.add_photos),
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight(600),
                            color = Gray,
                        )
                    )
                }
                Divider(color = LightBlue)
                AgendaDateTime(
                    label = stringResource(R.string.from),
                    time = eventUiState.fromTime,
                    date = eventUiState.fromDate,
                    onTimeClick = { /*TODO*/ },
                    onDateClick = { /*TODO*/ },
                    isReadOnly = !eventUiState.isEditing,
                    modifier = Modifier.fillMaxWidth()
                )
                Divider(color = LightBlue)
                AgendaDateTime(
                    label = stringResource(R.string.to),
                    time = eventUiState.toTime,
                    date = eventUiState.toDate,
                    onTimeClick = { /*TODO*/ },
                    onDateClick = { /*TODO*/ },
                    isReadOnly = !eventUiState.isEditing,
                    modifier = Modifier.fillMaxWidth()
                )
                Divider(color = LightBlue)
                AgendaReminder(
                    onClick = { /*TODO*/ },
                    isReadOnly = !eventUiState.isEditing,
                )
                Divider(color = LightBlue)
                Spacer(Modifier.weight(1f))
                if (!eventUiState.isNew) {
                    TaskyTextButton(
                        text = stringResource(R.string.delete_event),
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
            eventUiState = EventState(),
        )
    }
}