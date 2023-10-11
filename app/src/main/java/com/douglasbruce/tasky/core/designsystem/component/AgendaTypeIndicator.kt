package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.douglasbruce.tasky.core.designsystem.theme.DarkGray

@Composable
fun AgendaTypeIndicator(
    color: Color,
    agendaType: String,
    modifier: Modifier = Modifier,
    borderColor: Color = Color.Transparent,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            Modifier
                .padding(end = 12.dp)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(size = 2.dp)
                )
                .width(24.dp)
                .height(24.dp)
                .background(
                    color = color,
                    shape = RoundedCornerShape(size = 2.dp),
                ),
        )
        Text(
            text = agendaType,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 19.2.sp,
                fontWeight = FontWeight(600),
                color = DarkGray,
            )
        )
    }
}