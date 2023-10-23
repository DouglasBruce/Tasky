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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaReminder(
    onSelectionClick: () -> Unit,
    modifier: Modifier = Modifier,
    agendaReminderState: AgendaReminderState = rememberAgendaReminderState(),
    isReadOnly: Boolean = false,
) {
    Box(
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
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
                    text = agendaReminderState.selectedNotificationType.value.text.asString(),
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
                agendaReminderState.expanded.value = true
            }
        )
        DropdownMenu(
            expanded = agendaReminderState.expanded.value,
            onDismissRequest = { agendaReminderState.expanded.value = false },
            offset = DpOffset(16.dp, 4.dp)
        ) {
            TaskyDropdownMenuItem(
                text = stringResource(R.string.ten_minutes_before),
                onClick = {
                    agendaReminderState.selectedNotificationType.value = NotificationType.TEN_MINUTES
                    agendaReminderState.expanded.value = false
                    onSelectionClick()
                },
            )
            TaskyDropdownMenuItem(
                text = stringResource(R.string.thirty_minutes_before),
                onClick = {
                    agendaReminderState.selectedNotificationType.value =
                        NotificationType.THIRTY_MINUTES
                    agendaReminderState.expanded.value = false
                    onSelectionClick()
                },
            )
            TaskyDropdownMenuItem(
                text = stringResource(R.string.one_hour_before),
                onClick = {
                    agendaReminderState.selectedNotificationType.value = NotificationType.ONE_HOUR
                    agendaReminderState.expanded.value = false
                    onSelectionClick()
                },
            )
            TaskyDropdownMenuItem(
                text = stringResource(R.string.six_hours_before),
                onClick = {
                    agendaReminderState.selectedNotificationType.value = NotificationType.SIX_HOURS
                    agendaReminderState.expanded.value = false
                    onSelectionClick()
                },
            )
            TaskyDropdownMenuItem(
                text = stringResource(R.string.one_day_before),
                onClick = {
                    agendaReminderState.selectedNotificationType.value = NotificationType.ONE_DAY
                    agendaReminderState.expanded.value = false
                    onSelectionClick()
                },
            )
        }
    }
}

@Composable
@ExperimentalMaterial3Api
fun rememberAgendaReminderState(
    initialSelectedNotificationType: NotificationType = NotificationType.THIRTY_MINUTES,
    initiallyExpanded: Boolean = false,
): AgendaReminderState = rememberSaveable(
    saver = AgendaReminderState.Saver()
) {
    AgendaReminderState(
        initialSelectedNotificationType = initialSelectedNotificationType,
        initiallyExpanded = initiallyExpanded,
    )
}

@Stable
class AgendaReminderState constructor(
    initialSelectedNotificationType: NotificationType = NotificationType.THIRTY_MINUTES,
    initiallyExpanded: Boolean = false
) {

    var selectedNotificationType = mutableStateOf(NotificationType.THIRTY_MINUTES)
    var expanded = mutableStateOf(false)

    init {
        selectedNotificationType.value = initialSelectedNotificationType
        expanded.value = initiallyExpanded
    }

    companion object {
        fun Saver(): Saver<AgendaReminderState, Any> = listSaver(
            save = {
                listOf(
                    it.selectedNotificationType.value,
                    it.expanded.value
                )
            },
            restore = { value ->
                AgendaReminderState(
                    initialSelectedNotificationType = value[0] as NotificationType,
                    initiallyExpanded = value[1] as Boolean
                )
            }
        )
    }
}