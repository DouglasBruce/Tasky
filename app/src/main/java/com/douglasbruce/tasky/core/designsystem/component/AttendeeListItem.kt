package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import com.douglasbruce.tasky.core.designsystem.theme.DarkGray
import com.douglasbruce.tasky.core.designsystem.theme.Gray
import com.douglasbruce.tasky.core.designsystem.theme.LightBlueVariant
import com.douglasbruce.tasky.core.designsystem.theme.LightGrayBlue
import com.douglasbruce.tasky.core.designsystem.theme.White
import com.douglasbruce.tasky.core.domain.formatter.NameFormatter

@Composable
fun AttendeeItem(
    name: String,
    id: String,
    onDeleteAttendee: (String) -> Unit,
    modifier: Modifier = Modifier,
    isHost: Boolean = false,
    isEditing: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(LightBlueVariant, RoundedCornerShape(10.dp))
            .padding(8.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Gray),
        ) {
            val nameFormatter = NameFormatter()
            Text(
                text = nameFormatter.getInitials(name).uppercase(),
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 14.4.sp,
                    fontWeight = FontWeight(600),
                    color = White,
                )
            )
        }
        Text(
            text = name,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 12.sp,
                fontWeight = FontWeight(500),
                color = DarkGray,
            ),
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp, end = 8.dp),
        )
        if (isHost) {
            Text(
                text = stringResource(R.string.creator),
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight(500),
                    color = LightGrayBlue,
                ),
                modifier = modifier.padding(end = 8.dp),
            )
        } else if (isEditing) {
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = DarkGray
                ),
                onClick = { onDeleteAttendee(id) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = TaskyIcons.DeleteOutlined,
                    contentDescription = stringResource(R.string.delete),
                )
            }
        }
    }
}

@Preview
@Composable
fun CreatorAttendeeItemPreview() {
    AttendeeItem(
        name = "Test User",
        id = "1",
        onDeleteAttendee = {},
        isHost = true,
        isEditing = false
    )
}

@Preview
@Composable
fun AttendeeItemPreview() {
    AttendeeItem(
        name = "Test User",
        id = "1",
        onDeleteAttendee = {},
        isHost = false,
        isEditing = false
    )
}

@Preview
@Composable
fun IsEditableAttendeeItemPreview() {
    AttendeeItem(
        name = "Test User",
        id = "1",
        onDeleteAttendee = {},
        isHost = false,
        isEditing = true
    )
}