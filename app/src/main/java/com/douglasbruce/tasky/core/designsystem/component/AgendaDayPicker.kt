package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalDate

@Composable
fun AgendaDayPicker(
    date: LocalDate,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    selectedDay: Int = 0,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier.fillMaxWidth(),
    ) {
        for (i in 0..6) {
            AgendaDayPickerItem(
                day = date.plusDays(i.toLong()),
                isSelected = selectedDay == i,
                onClick = { onClick(i) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AgendaDayPickerPreview() {
    AgendaDayPicker(
        date = LocalDate.now(),
        selectedDay = 2,
        onClick = {}
    )
}