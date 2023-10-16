package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.Black
import com.douglasbruce.tasky.core.designsystem.theme.Gray
import com.douglasbruce.tasky.core.designsystem.theme.LightBlueVariant

@Composable
fun AgendaReminder(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false,
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
                text = stringResource(R.string.thirty_minutes_before),
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
        modifier = modifier.clickable(enabled = !isReadOnly) { onClick() }
    )
}