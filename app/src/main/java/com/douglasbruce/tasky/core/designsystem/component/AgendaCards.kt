package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
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
        modifier = modifier,
    )
}

@Composable
fun AgendaEventCard(
    title: String,
    content: String,
    dateTime: String,
    modifier: Modifier = Modifier,
) {
    NonInteractiveAgendaCard(
        cardColor = LightGreen,
        titleColor = Black,
        contentTextColor = DarkGray,
        title = title,
        content = content,
        dateTime = dateTime,
        modifier = modifier,
    )
}

@Composable
fun AgendaReminderCard(
    title: String,
    content: String,
    dateTime: String,
    modifier: Modifier = Modifier,
) {
    NonInteractiveAgendaCard(
        cardColor = LightBlueVariant,
        titleColor = Black,
        contentTextColor = DarkGray,
        title = title,
        content = content,
        dateTime = dateTime,
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
    modifier: Modifier = Modifier,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        modifier = modifier,
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
                        onClick = { /*TODO*/ },
                    )
                    TaskyDropdownMenuItem(
                        text = stringResource(R.string.edit),
                        onClick = { /*TODO*/ },
                    )
                    TaskyDropdownMenuItem(
                        text = stringResource(R.string.delete),
                        onClick = { /*TODO*/ },
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
        )
    }
}