package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.Black
import com.douglasbruce.tasky.core.domain.validation.ErrorType

@Composable
fun AttendeeDialog(
    emailAddress: String,
    onValueChange: (String) -> Unit,
    onClose: () -> Unit,
    onAdd: () -> Unit,
    isValid: Boolean,
    isChecking: Boolean,
    errorType: ErrorType,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = { onClose() },
        confirmButton = {
            TaskyButton(
                text = stringResource(R.string.add),
                onClick = { onAdd() },
                modifier = modifier.fillMaxWidth(),
                enabled = isValid,
                isLoading = isChecking,
            )
        },
        icon = {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = TaskyIcons.Close,
                        contentDescription = stringResource(R.string.close),
                        tint = Black,
                    )
                }
            }
        },
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.add_visitor),
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(700),
                        color = Black,
                    ),
                )
            }
        },
        text = {
            TaskyTextField(
                value = emailAddress,
                onValueChange = { onValueChange(it) },
                isError = errorType != ErrorType.NONE,
                isValid = isValid,
                placeholder = stringResource(R.string.email_placeholder),
                supportingText = when (errorType) {
                    ErrorType.EMPTY -> {
                        { Text(text = stringResource(R.string.error_email_empty)) }
                    }

                    ErrorType.FORMAT -> {
                        { Text(text = stringResource(R.string.error_email_format)) }
                    }

                    ErrorType.DOES_NOT_EXIST -> {
                        { Text(text = stringResource(R.string.user_does_not_exist)) }
                    }

                    else -> null
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done,
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    )
}

@Preview
@Composable
fun AttendeeDialogPreview() {
    AttendeeDialog(
        emailAddress = "test@email.com",
        onValueChange = {},
        onClose = {},
        onAdd = {},
        isValid = true,
        isChecking = false,
        errorType = ErrorType.NONE
    )
}