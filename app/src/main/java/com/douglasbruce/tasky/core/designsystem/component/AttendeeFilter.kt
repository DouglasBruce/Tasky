package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.douglasbruce.tasky.core.designsystem.theme.Black
import com.douglasbruce.tasky.core.designsystem.theme.DarkGray
import com.douglasbruce.tasky.core.designsystem.theme.LightBlueVariant
import com.douglasbruce.tasky.core.designsystem.theme.White

@Composable
fun AttendeeFilter(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (selected) Black else LightBlueVariant
    val textColor = if (selected) White else DarkGray

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .width(100.dp)
            .clickable { onClick() }
            .background(color = backgroundColor, shape = RoundedCornerShape(size = 100.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight(500),
                color = textColor,
            )
        )
    }
}

@Preview
@Composable
fun AttendeeFilterPreview() {
    AttendeeFilter(
        text = "All",
        selected = false,
        onClick = {}
    )
}

@Preview
@Composable
fun SelectedAttendeeFilterPreview() {
    AttendeeFilter(
        text = "Not going",
        selected = true,
        onClick = {}
    )
}