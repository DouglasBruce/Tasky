package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.douglasbruce.tasky.core.designsystem.theme.Black

@Composable
fun TimeNeedle(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(12.dp)
            .drawBehind {
                drawCircle(
                    color = Black,
                    radius = 6.dp.toPx(),
                    center = Offset(6.dp.toPx(), center.y)
                )
            },
    ) {
        Divider(
            modifier = Modifier.padding(start = 2.dp),
            thickness = 2.dp,
            color = Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimeNeedlePreview() {
    TimeNeedle(modifier = Modifier.fillMaxWidth())
}