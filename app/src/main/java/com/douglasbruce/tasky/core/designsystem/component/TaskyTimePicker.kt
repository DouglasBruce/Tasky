package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.designsystem.theme.Black
import com.douglasbruce.tasky.core.designsystem.theme.DarkGray
import com.douglasbruce.tasky.core.designsystem.theme.LightBlueVariant
import com.douglasbruce.tasky.core.designsystem.theme.LinkBlue
import com.douglasbruce.tasky.core.designsystem.theme.Orange
import com.douglasbruce.tasky.core.designsystem.theme.White

@ExperimentalMaterial3Api
@Composable
fun TaskyTimePicker(
    timePickerState: TimePickerState,
    onOkClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = DatePickerDefaults.shape,
    tonalElevation: Dp = DatePickerDefaults.TonalElevation,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier.wrapContentHeight(),
        properties = properties
    ) {
        Surface(
            modifier = Modifier
                .requiredWidth(320.0.dp)
                .heightIn(max = 568.0.dp),
            shape = shape,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = tonalElevation,
        ) {
            val initialDisplayMode = remember {
                mutableStateOf(DisplayMode.Picker)
            }

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                TimePickerTitle(
                    initialDisplayMode.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 12.dp, top = 16.dp, bottom = 16.dp)
                )
                TimePickerContent(
                    timePickerState = timePickerState,
                    displayMode = initialDisplayMode.value,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = LightBlueVariant,
                        clockDialSelectedContentColor = White,
                        clockDialUnselectedContentColor = DarkGray,
                        selectorColor = Black,
                        timeSelectorSelectedContainerColor = Orange,
                        timeSelectorUnselectedContainerColor = LightBlueVariant,
                        timeSelectorSelectedContentColor = DarkGray,
                        timeSelectorUnselectedContentColor = DarkGray,
                        periodSelectorSelectedContainerColor = LinkBlue,
                    ),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp, bottom = 8.dp, end = 6.dp)
                ) {
                    DisplayModeToggleButton(
                        displayMode = initialDisplayMode.value,
                        onDisplayModeChange = { displayMode ->
                            initialDisplayMode.value = displayMode
                        },
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    TextButton(onClick = onOkClick) {
                        Text(stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerContent(
    timePickerState: TimePickerState,
    displayMode: DisplayMode,
    colors: TimePickerColors = TimePickerDefaults.colors(),
) {
    when (displayMode) {
        DisplayMode.Picker -> TimePicker(
            state = timePickerState,
            colors = colors
        )

        DisplayMode.Input -> TimeInput(
            state = timePickerState,
            colors = colors
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerTitle(displayMode: DisplayMode, modifier: Modifier = Modifier) {
    when (displayMode) {
        DisplayMode.Picker -> Text(
            text = stringResource(R.string.select_time),
            modifier = modifier
        )

        DisplayMode.Input -> Text(
            text = stringResource(R.string.enter_time),
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DisplayModeToggleButton(
    displayMode: DisplayMode,
    onDisplayModeChange: (DisplayMode) -> Unit
) {
    if (displayMode == DisplayMode.Picker) {
        IconButton(onClick = { onDisplayModeChange(DisplayMode.Input) }) {
            Icon(
                imageVector = Icons.Outlined.Keyboard,
                contentDescription = stringResource(R.string.switch_text_input),
            )
        }
    } else {
        IconButton(onClick = { onDisplayModeChange(DisplayMode.Picker) }) {
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = stringResource(R.string.switch_touch_input),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TimePickerDialogPreview() {
    TaskyTimePicker(
        timePickerState = TimePickerState(
            initialHour = 8,
            initialMinute = 30,
            is24Hour = false,
        ),
        onOkClick = {},
        onDismiss = {},
    )
}