package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.common.utils.DateUtils
import com.douglasbruce.tasky.core.designsystem.theme.DarkGray
import com.douglasbruce.tasky.core.designsystem.theme.LinkBlue
import com.douglasbruce.tasky.core.designsystem.theme.Orange
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskyDatePicker(
    datePickerState: DatePickerState,
    onOkClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    val confirmEnabled by remember { derivedStateOf { datePickerState.selectedDateMillis != null } }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onOkClick,
                enabled = confirmEnabled
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                todayDateBorderColor = LinkBlue,
                selectedDayContainerColor = Orange,
                selectedDayContentColor = DarkGray,
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TaskyDatePickerPreview() {
    TaskyDatePicker(
        datePickerState = DatePickerState(
            initialSelectedDateMillis = DateUtils.getDateMilli(LocalDate.now().plusDays(1)),
            initialDisplayedMonthMillis = null,
            yearRange = DatePickerDefaults.YearRange,
            initialDisplayMode = DisplayMode.Picker
        ),
        onOkClick = {},
        onDismiss = {}
    )
}