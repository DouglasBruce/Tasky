package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
fun AgendaDescription(
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false,
) {
    ListItem(
        headlineContent = {
            Text(
                text = description,
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