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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
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
                overflow = TextOverflow.Ellipsis,
                maxLines = 5,
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

@Preview
@Composable
fun AgendaDescriptionPreview() {
    AgendaDescription(
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex",
        onClick = {}
    )
}