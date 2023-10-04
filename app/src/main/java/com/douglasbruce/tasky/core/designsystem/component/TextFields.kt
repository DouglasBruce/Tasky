package com.douglasbruce.tasky.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.DarkGray
import com.douglasbruce.tasky.core.designsystem.theme.DarkGrayBlue
import com.douglasbruce.tasky.core.designsystem.theme.Gray
import com.douglasbruce.tasky.core.designsystem.theme.LightBlueVariant
import com.douglasbruce.tasky.core.designsystem.theme.LightGrayBlue

@Composable
fun TaskyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    isValid: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    TaskyTextField(
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        placeholder = placeholder,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = LightBlueVariant,
            unfocusedContainerColor = LightBlueVariant,
            errorContainerColor = LightBlueVariant,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = LightGrayBlue,
            errorBorderColor = MaterialTheme.colorScheme.error,
            unfocusedPlaceholderColor = DarkGrayBlue,
            focusedPlaceholderColor = DarkGrayBlue,
            errorPlaceholderColor = DarkGrayBlue,
            unfocusedTextColor = DarkGray,
            focusedTextColor = DarkGray,
            errorTextColor = DarkGray,
            cursorColor = MaterialTheme.colorScheme.primary,
            errorCursorColor = MaterialTheme.colorScheme.error,
            focusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,
        ),
        keyboardOptions = keyboardOptions,
        trailingIcon = {
            AnimatedVisibility(visible = isValid) {
                Icon(imageVector = TaskyIcons.Check, contentDescription = null)
            }
        },
        supportingText = supportingText,
        modifier = modifier,
    )
}

@Composable
fun TaskyPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTrailingIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
) {
    TaskyTextField(
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        placeholder = stringResource(R.string.password_placeholder),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = LightBlueVariant,
            unfocusedContainerColor = LightBlueVariant,
            errorContainerColor = LightBlueVariant,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = LightGrayBlue,
            errorBorderColor = MaterialTheme.colorScheme.error,
            unfocusedPlaceholderColor = DarkGrayBlue,
            focusedPlaceholderColor = DarkGrayBlue,
            errorPlaceholderColor = DarkGrayBlue,
            unfocusedTextColor = DarkGray,
            focusedTextColor = DarkGray,
            errorTextColor = DarkGray,
            cursorColor = MaterialTheme.colorScheme.primary,
            errorCursorColor = MaterialTheme.colorScheme.error,
            focusedTrailingIconColor = Gray,
            unfocusedTrailingIconColor = Gray,
            errorTrailingIconColor = MaterialTheme.colorScheme.error,
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
        ),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                TaskyIcons.Visibility
            else TaskyIcons.VisibilityOff

            val description = if (passwordVisible)
                stringResource(id = R.string.hide_password)
            else stringResource(id = R.string.show_password)

            IconButton(onClick = onTrailingIconClick) {
                Icon(imageVector = image, description)
            }
        },
        supportingText = supportingText,
        modifier = modifier,
    )
}

@Composable
private fun TaskyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    placeholder: String,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight(400),
                    letterSpacing = 0.8.sp,
                ),
            )
        },
        singleLine = true,
        isError = isError,
        colors = colors,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        visualTransformation = visualTransformation,
        textStyle = TextStyle(
            fontSize = 16.sp,
            lineHeight = 30.sp,
            fontWeight = FontWeight(400),
            letterSpacing = 0.8.sp,
        ),
        modifier = modifier,
    )
}