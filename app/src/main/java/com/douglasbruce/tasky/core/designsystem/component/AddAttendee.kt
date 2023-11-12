package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.Black
import com.douglasbruce.tasky.core.designsystem.theme.Gray
import com.douglasbruce.tasky.core.designsystem.theme.LightBlueVariant

@Composable
fun AddAttendeeButton(
    isEditing: Boolean,
    onAddAttendeeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .requiredHeightIn(min = 48.dp)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.visitors),
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight(700),
                color = Black,
            )
        )
        Spacer(Modifier.width(8.dp))
        AnimatedVisibility(
            visible = isEditing,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(LightBlueVariant)
                    .clickable { onAddAttendeeClick() },
            ) {
                Icon(
                    imageVector = TaskyIcons.Add,
                    contentDescription = stringResource(R.string.add_visitors),
                    tint = Gray,
                )
            }
        }
    }
}

@Preview
@Composable
fun AddAttendeeButtonPreview() {
    AddAttendeeButton(
        isEditing = true,
        onAddAttendeeClick = {},
    )
}