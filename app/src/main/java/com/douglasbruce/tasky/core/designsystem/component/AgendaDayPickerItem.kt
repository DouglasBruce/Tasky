package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.douglasbruce.tasky.core.designsystem.theme.DarkGray
import com.douglasbruce.tasky.core.designsystem.theme.Gray
import com.douglasbruce.tasky.core.designsystem.theme.Orange
import java.time.LocalDate

@Composable
fun AgendaDayPickerItem(
    day: LocalDate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(100))
            .background(if (isSelected) Orange else Color.Transparent)
            .clickable { onClick() }
            .defaultMinSize(minWidth = 36.dp)
            .padding(8.dp),
    ) {
        Text(
            text = day.dayOfWeek.name[0].toString(),
            style = TextStyle(
                fontSize = 11.sp,
                lineHeight = 13.2.sp,
                fontWeight = FontWeight(700),
                color = if (isSelected) DarkGray else Gray,
            )
        )
        Text(
            text = day.dayOfMonth.toString(),
            style = TextStyle(
                fontSize = 17.sp,
                lineHeight = 20.4.sp,
                fontWeight = FontWeight(700),
                color = DarkGray,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AgendaDayPickerItemPreview() {
    AgendaDayPickerItem(
        day = LocalDate.now(),
        isSelected = true,
        onClick = {}
    )
}