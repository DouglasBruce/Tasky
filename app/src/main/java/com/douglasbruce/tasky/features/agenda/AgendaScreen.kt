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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.designsystem.component.TaskyDropdownMenuItem
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.Black
import com.douglasbruce.tasky.core.designsystem.theme.LightBlue
import com.douglasbruce.tasky.core.designsystem.theme.LightGrayBlue
import com.douglasbruce.tasky.core.designsystem.theme.TaskyTheme
import com.douglasbruce.tasky.core.designsystem.theme.White

@Composable
internal fun AgendaRoute(
    onLogoutClick: () -> Unit,
    onAddEventClick: (String?) -> Unit,
    onAddTaskClick: (String?) -> Unit,
    onAddReminderClick: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    AgendaScreen(
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
    onLogoutClick: () -> Unit,
    onAddEventClick: (String?) -> Unit,
    onAddTaskClick: (String?) -> Unit,
    onAddReminderClick: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    //TODO: Hoist state to viewModel
    var expanded by remember {
        mutableStateOf(false)
    }
    var expanded2 by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    //TODO: Hoist selected month to viewModel
                    Text(text = "October")
                },
                actions = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(LightBlue)
                            .clickable { expanded = !expanded },
                    ) {
                        Text(
                            //TODO: Hoist initials to viewModel
                            text = "DB", style = TextStyle(
                                fontSize = 13.sp,
                                fontWeight = FontWeight(600),
                                color = LightGrayBlue,
                            )
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            TaskyDropdownMenuItem(
                                text = stringResource(R.string.logout),
                                onClick = onLogoutClick,
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
                    onClick = { expanded2 = !expanded2 },
                ) {
                    Icon(imageVector = TaskyIcons.Add, contentDescription = null)
                }
                DropdownMenu(
                    expanded = expanded2,
                    onDismissRequest = { expanded2 = false },
                ) {
                    TaskyDropdownMenuItem(
                        text = stringResource(R.string.event),
                        onClick = { onAddEventClick(null) },
                    )
                    TaskyDropdownMenuItem(
                        text = stringResource(R.string.task),
                        onClick = { onAddTaskClick(null) },
                    )
                    TaskyDropdownMenuItem(
                        text = stringResource(R.string.reminder),
                        onClick = { onAddReminderClick(null) },
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
                    text = stringResource(R.string.today),
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
            onLogoutClick = {},
            onAddEventClick = {},
            onAddTaskClick = {},
            onAddReminderClick = {},
        )
    }
}