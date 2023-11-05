package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.Black
import com.douglasbruce.tasky.core.designsystem.theme.Gray
import com.douglasbruce.tasky.core.designsystem.theme.LightBlueVariant
import com.douglasbruce.tasky.core.model.NotificationType

@Composable
fun AgendaReminder(
    notificationType: NotificationType,
    onSelectionClick: (NotificationType) -> Unit,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false,
) {
    Box(
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        var expanded by remember {
            mutableStateOf(false)
        }
        ListItem(
            leadingContent = {
                Icon(
                    imageVector = TaskyIcons.NotificationsOutlined,
                    contentDescription = null,
                    tint = Gray,
                    modifier = Modifier
                        .background(
                            color = LightBlueVariant,
                            shape = RoundedCornerShape(size = 5.dp)
                        )
                        .padding(4.dp)
                )
            },
            headlineContent = {
                Text(
                    text = notificationType.text.asString(),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 15.sp,
                        fontWeight = FontWeight(400),
                        color = Black,
                    )
                )
            },
            trailingContent = {
                AnimatedVisibility(
                    visible = !isReadOnly,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Icon(
                        imageVector = TaskyIcons.ChevronRight,
                        contentDescription = null,
                        tint = Black,
                    )
                }
            },
            modifier = modifier.clickable(enabled = !isReadOnly) {
                expanded = true
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(16.dp, 4.dp)
        ) {
            TaskyDropdownMenuItem(
                text = stringResource(R.string.ten_minutes_before),
                onClick = {
                    expanded = false
                    onSelectionClick(NotificationType.TEN_MINUTES)
                },
            )
            TaskyDropdownMenuItem(
                text = stringResource(R.string.thirty_minutes_before),
                onClick = {
                    expanded = false
                    onSelectionClick(NotificationType.THIRTY_MINUTES)
                },
            )
            TaskyDropdownMenuItem(
                text = stringResource(R.string.one_hour_before),
                onClick = {
                    expanded = false
                    onSelectionClick(NotificationType.ONE_HOUR)
                },
            )
            TaskyDropdownMenuItem(
                text = stringResource(R.string.six_hours_before),
                onClick = {
                    expanded = false
                    onSelectionClick(NotificationType.SIX_HOURS)
                },
            )
            TaskyDropdownMenuItem(
                text = stringResource(R.string.one_day_before),
                onClick = {
                    expanded = false
                    onSelectionClick(NotificationType.ONE_DAY)
                },
            )
        }
    }
}