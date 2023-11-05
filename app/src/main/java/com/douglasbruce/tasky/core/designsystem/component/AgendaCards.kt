package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.Black
import com.douglasbruce.tasky.core.designsystem.theme.DarkGray
import com.douglasbruce.tasky.core.designsystem.theme.Green
import com.douglasbruce.tasky.core.designsystem.theme.LightBlueVariant
import com.douglasbruce.tasky.core.designsystem.theme.LightGreen
import com.douglasbruce.tasky.core.designsystem.theme.TaskyTheme
import com.douglasbruce.tasky.core.designsystem.theme.White

@Composable
fun AgendaTaskCard(
    title: String,
    content: String,
    dateTime: String,
    isDone: Boolean,
    onLeadingIconClick: () -> Unit,
    onOpenOptionClick: () -> Unit,
    onEditOptionClick: () -> Unit,
    onDeleteOptionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    InteractiveAgendaCard(
        cardColor = Green,
        titleColor = White,
        contentTextColor = White,
        title = title,
        content = content,
        dateTime = dateTime,
        isDone = isDone,
        onLeadingIconClick = onLeadingIconClick,
        onOpenOptionClick = onOpenOptionClick,
        onEditOptionClick = onEditOptionClick,
        onDeleteOptionClick = onDeleteOptionClick,
        dialogTitle = stringResource(R.string.delete_task),
        message = stringResource(R.string.delete_task_confirmation),
        modifier = modifier,
    )
}

@Composable
fun AgendaEventCard(
    title: String,
    content: String,
    dateTime: String,
    onOpenOptionClick: () -> Unit,
    onEditOptionClick: () -> Unit,
    onDeleteOptionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NonInteractiveAgendaCard(
        cardColor = LightGreen,
        titleColor = Black,
        contentTextColor = DarkGray,
        title = title,
        content = content,
        dateTime = dateTime,
        onOpenOptionClick = onOpenOptionClick,
        onEditOptionClick = onEditOptionClick,
        onDeleteOptionClick = onDeleteOptionClick,
        dialogTitle = stringResource(R.string.delete_event),
        message = stringResource(R.string.delete_event_confirmation),
        modifier = modifier,
    )
}

@Composable
fun AgendaReminderCard(
    title: String,
    content: String,
    dateTime: String,
    onOpenOptionClick: () -> Unit,
    onEditOptionClick: () -> Unit,
    onDeleteOptionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NonInteractiveAgendaCard(
        cardColor = LightBlueVariant,
        titleColor = Black,
        contentTextColor = DarkGray,
        title = title,
        content = content,
        dateTime = dateTime,
        onOpenOptionClick = onOpenOptionClick,
        onEditOptionClick = onEditOptionClick,
        onDeleteOptionClick = onDeleteOptionClick,
        dialogTitle = stringResource(R.string.delete_reminder),
        message = stringResource(R.string.delete_reminder_confirmation),
        modifier = modifier,
    )
}

@Composable
private fun NonInteractiveAgendaCard(
    cardColor: Color,
    titleColor: Color,
    contentTextColor: Color,
    title: String,
    content: String,
    dateTime: String,
    onOpenOptionClick: () -> Unit,
    onEditOptionClick: () -> Unit,
    onDeleteOptionClick: () -> Unit,
    dialogTitle: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    AgendaCard(
        cardColor = cardColor,
        titleColor = titleColor,
        contentTextColor = contentTextColor,
        content = content,
        dateTime = dateTime,
        leadingIcon = {
            Icon(
                imageVector = TaskyIcons.CircleOutlined,
                contentDescription = null,
                tint = titleColor,
                modifier = Modifier.minimumInteractiveComponentSize(),
            )
        },
        title = {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = TextStyle(
                    fontSize = 20.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(600),
                    color = titleColor,
                ),
                modifier = Modifier.weight(1f),
            )
        },
        onOpenOptionClick = onOpenOptionClick,
        onEditOptionClick = onEditOptionClick,
        onDeleteOptionClick = onDeleteOptionClick,
        dialogTitle = dialogTitle,
        message = message,
        modifier = modifier,
    )
}

@Composable
private fun InteractiveAgendaCard(
    cardColor: Color,
    titleColor: Color,
    contentTextColor: Color,
    title: String,
    content: String,
    dateTime: String,
    onLeadingIconClick: () -> Unit,
    onOpenOptionClick: () -> Unit,
    onEditOptionClick: () -> Unit,
    onDeleteOptionClick: () -> Unit,
    dialogTitle: String,
    message: String,
    modifier: Modifier = Modifier,
    isDone: Boolean = false,
) {
    AgendaCard(
        cardColor = cardColor,
        titleColor = titleColor,
        contentTextColor = contentTextColor,
        content = content,
        dateTime = dateTime,
        leadingIcon = {
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = titleColor,
                ),
                onClick = onLeadingIconClick
            ) {
                Crossfade(targetState = isDone, label = "isDoneAnimation") { isChecked ->
                    if (isChecked) {
                        Icon(
                            imageVector = TaskyIcons.CheckCircleOutlined,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            imageVector = TaskyIcons.CircleOutlined,
                            contentDescription = null
                        )
                    }
                }
            }
        },
        title = {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = TextStyle(
                    fontSize = 20.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(600),
                    color = titleColor,
                    textDecoration = if (isDone) TextDecoration.LineThrough else null
                ),
                modifier = Modifier.weight(1f),
            )
        },
        onOpenOptionClick = onOpenOptionClick,
        onEditOptionClick = onEditOptionClick,
        onDeleteOptionClick = onDeleteOptionClick,
        dialogTitle = dialogTitle,
        message = message,
        modifier = modifier,
    )
}

@Composable
private fun AgendaCard(
    cardColor: Color,
    titleColor: Color,
    title: @Composable RowScope.() -> Unit,
    contentTextColor: Color,
    content: String,
    dateTime: String,
    leadingIcon: @Composable () -> Unit,
    onOpenOptionClick: () -> Unit,
    onEditOptionClick: () -> Unit,
    onDeleteOptionClick: () -> Unit,
    dialogTitle: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var showDeleteConfirmation by remember {
        mutableStateOf(false)
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            confirmButton = {
                TextButton(onClick = { onDeleteOptionClick() }) {
                    Text(stringResource(R.string.ok))
                }
            },
            title = { Text(dialogTitle) },
            text = { Text(message) }
        )
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        modifier = modifier.clickable(onClick = { onOpenOptionClick() }),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            leadingIcon()
            title()
            Box(
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .wrapContentSize(Alignment.TopStart)
            ) {
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = titleColor,
                    ),
                    onClick = { expanded = !expanded },
                ) {
                    Icon(
                        imageVector = TaskyIcons.MoreHoriz,
                        contentDescription = stringResource(R.string.more_options),
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    TaskyDropdownMenuItem(
                        text = stringResource(R.string.open),
                        onClick = {
                            expanded = false
                            onOpenOptionClick()
                        },
                    )
                    TaskyDropdownMenuItem(
                        text = stringResource(R.string.edit),
                        onClick = {
                            expanded = false
                            onEditOptionClick()
                        },
                    )
                    TaskyDropdownMenuItem(
                        text = stringResource(R.string.delete),
                        onClick = {
                            expanded = false
                            showDeleteConfirmation = true
                        },
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 48.dp,
                    end = 12.dp,
                ),
        ) {
            Text(
                text = content,
                overflow = TextOverflow.Ellipsis,
                maxLines = 5,
                minLines = 2,
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight(400),
                    color = contentTextColor,
                )
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 12.dp,
                    top = 4.dp,
                    end = 12.dp,
                    bottom = 12.dp,
                ),
        ) {
            Text(
                text = dateTime,
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight(300),
                    color = contentTextColor,
                    textAlign = TextAlign.Right,
                )
            )
        }
    }
}

@Preview
@Composable
fun AgendaTaskCardPreview() {
    TaskyTheme {
        AgendaTaskCard(
            title = "Task",
            content = "",
            dateTime = "Mar 5, 10:00",
            isDone = true,
            onLeadingIconClick = {},
            onOpenOptionClick = {},
            onEditOptionClick = {},
            onDeleteOptionClick = {}
        )
    }
}

@Preview
@Composable
fun AgendaEventCardPreview() {
    TaskyTheme {
        AgendaEventCard(
            title = "Lorem ipsum dolor sit amet, consectetur",
            content = "This is an event",
            dateTime = "Mar 5, 10:30 - Mar 5, 11:00",
            onOpenOptionClick = {},
            onEditOptionClick = {},
            onDeleteOptionClick = {}
        )
    }
}

@Preview
@Composable
fun AgendaReminderCardPreview() {
    TaskyTheme {
        AgendaReminderCard(
            title = "Reminder",
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit",
            dateTime = "Mar 5, 11:30",
            onOpenOptionClick = {},
            onEditOptionClick = {},
            onDeleteOptionClick = {}
        )
    }
}