package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.Black

@Composable
fun AgendaTitle(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false,
) {
    ListItem(
        leadingContent = {
            Icon(
                imageVector = TaskyIcons.CircleOutlined,
                contentDescription = null,
                tint = Black,
            )
        },
        headlineContent = {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 26.sp,
                    lineHeight = 25.sp,
                    fontWeight = FontWeight(700),
                    color = Black,
                ),
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
        },
        modifier = modifier.clickable(enabled = !isReadOnly) { onClick() }
    )
}