package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.Black
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun AgendaDateTime(
    label: String,
    time: LocalTime,
    date: LocalDate,
    onTimeClick: () -> Unit,
    onDateClick: () -> Unit,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false,
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val formattedTime = time.format(timeFormatter)
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd uuuu")
    val formattedDate = date.format(dateFormatter)

    Row(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable(enabled = !isReadOnly) { onTimeClick() }
        ) {
            ListItem(
                leadingContent = {
                    Text(
                        text = label,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 15.sp,
                            fontWeight = FontWeight(400),
                            color = Black,
                        ),
                        modifier = Modifier.width(40.dp)
                    )
                },
                headlineContent = {
                    Text(
                        text = formattedTime,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 15.sp,
                            fontWeight = FontWeight(400),
                            color = Black,
                        )
                    )
                },
                trailingContent = when (isReadOnly) {
                    true -> null
                    else -> {
                        {
                            Icon(
                                imageVector = TaskyIcons.ChevronRight,
                                contentDescription = null,
                                tint = Black,
                            )
                        }
                    }
                }
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable(enabled = !isReadOnly) { onDateClick() }
        ) {
            ListItem(
                headlineContent = {
                    Text(
                        text = formattedDate,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 15.sp,
                            fontWeight = FontWeight(400),
                            color = Black,
                        )
                    )
                },
                trailingContent = when (isReadOnly) {
                    true -> null
                    else -> {
                        {
                            Icon(
                                imageVector = TaskyIcons.ChevronRight,
                                contentDescription = null,
                                tint = Black,
                            )
                        }
                    }
                }
            )
        }
    }
}