package com.douglasbruce.tasky.features.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.designsystem.component.TaskyButton
import com.douglasbruce.tasky.core.designsystem.component.TaskyHeader
import com.douglasbruce.tasky.core.designsystem.component.TaskyPasswordField
import com.douglasbruce.tasky.core.designsystem.component.TaskyTextField
import com.douglasbruce.tasky.core.designsystem.theme.DarkGrayBlue
import com.douglasbruce.tasky.core.designsystem.theme.LinkBlue
import com.douglasbruce.tasky.core.designsystem.theme.TaskyTheme
import com.douglasbruce.tasky.core.domain.validation.ErrorType
import com.douglasbruce.tasky.features.login.form.LoginFormEvent
import com.douglasbruce.tasky.features.login.form.LoginFormState

@Composable
internal fun LoginRoute(
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    LoginScreen(
        onSignUpClick = onSignUpClick,
        modifier = modifier.fillMaxSize(),
        uiState = viewModel.state,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun LoginScreen(
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    uiState: LoginFormState,
    onEvent: (LoginFormEvent) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = modifier,
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    )
                ),
        ) {
            TaskyHeader(
                title = stringResource(R.string.welcome_back),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
                    .padding(start = 16.dp, top = 48.dp, end = 16.dp, bottom = 40.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    TaskyTextField(
                        value = uiState.email,
                        onValueChange = {
                            onEvent(LoginFormEvent.EmailValueChanged(it))
                        },
                        isError = uiState.emailErrorType == ErrorType.EMPTY,
                        isValid = uiState.isEmailValid,
                        placeholder = stringResource(R.string.email_placeholder),
                        supportingText = when (uiState.emailErrorType) {
                            ErrorType.EMPTY -> {
                                { Text(text = stringResource(R.string.error_email_empty)) }
                            }

                            else -> null
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TaskyPasswordField(
                        value = uiState.password,
                        onValueChange = {
                            onEvent(LoginFormEvent.PasswordValueChanged(it))
                        },
                        isError = uiState.passwordErrorType != ErrorType.NONE,
                        supportingText = when (uiState.passwordErrorType) {
                            ErrorType.EMPTY -> {
                                { Text(text = stringResource(R.string.error_password_empty)) }
                            }

                            else -> null
                        },
                        passwordVisible = uiState.isPasswordVisible,
                        onTrailingIconClick = {
                            onEvent(LoginFormEvent.TogglePasswordVisibility)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    TaskyButton(
                        text = stringResource(R.string.log_in_button),
                        onClick = {
                            onEvent(LoginFormEvent.Submit)
                        },
                        enabled = uiState.isEmailValid && uiState.isPasswordValid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        Text(
                            text = stringResource(id = R.string.no_account),
                            style = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 30.sp,
                                fontWeight = FontWeight(500),
                                color = DarkGrayBlue,
                                letterSpacing = 0.7.sp,
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.sign_up),
                            style = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 30.sp,
                                fontWeight = FontWeight(500),
                                color = LinkBlue,
                                letterSpacing = 0.7.sp,
                            ),
                            modifier = Modifier.clickable(onClick = onSignUpClick)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    TaskyTheme {
        LoginScreen(
            onSignUpClick = {},
            uiState = LoginFormState(),
            onEvent = {}
        )
    }
}