package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.douglasbruce.tasky.core.designsystem.theme.DarkGray

@Composable
fun AttendeeListTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight(500),
            color = DarkGray,
        ),
        modifier = modifier,
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Preview(showBackground = true)
@Composable
fun AttendeeListPreview() {
    AttendeeListTitle(title = "Going")
}