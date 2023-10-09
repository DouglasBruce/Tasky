package com.douglasbruce.tasky.features.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.common.utils.UiText
import com.douglasbruce.tasky.core.designsystem.component.TaskyButton
import com.douglasbruce.tasky.core.designsystem.component.TaskyHeader
import com.douglasbruce.tasky.core.designsystem.component.TaskyPasswordField
import com.douglasbruce.tasky.core.designsystem.component.TaskyTextField
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.TaskyTheme
import com.douglasbruce.tasky.core.domain.validation.ErrorType
import com.douglasbruce.tasky.features.register.form.RegistrationFormEvent
import com.douglasbruce.tasky.features.register.form.RegistrationFormState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun RegisterRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    RegisterScreen(
        onBackClick = onBackClick,
        modifier = modifier.fillMaxSize(),
        uiState = viewModel.state,
        onEvent = viewModel::onEvent,
        errors = viewModel.errors,
        successes = viewModel.successes,
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class)
@Composable
internal fun RegisterScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    uiState: RegistrationFormState,
    onEvent: (RegistrationFormEvent) -> Unit,
    errors: Flow<UiText>,
    successes: Flow<UiText>,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(key1 = snackbarHostState) {
        errors.collect { error ->
            snackbarHostState.showSnackbar(
                message = error.asString(context),
                withDismissAction = true,
            )
        }
    }

    LaunchedEffect(key1 = snackbarHostState) {
        successes.collect { success ->
            val result = snackbarHostState.showSnackbar(
                message = success.asString(context),
                actionLabel = context.getString(R.string.login_action),
                withDismissAction = true,
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    onBackClick()
                }
                SnackbarResult.Dismissed -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = modifier,
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .consumeWindowInsets(paddingValues)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    )
                ),
        ) {
            TaskyHeader(
                title = stringResource(R.string.create_account),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
                    .padding(start = 16.dp, top = 48.dp, end = 16.dp, bottom = 40.dp)
            ) {
                TaskyTextField(
                    value = uiState.name,
                    onValueChange = {
                        onEvent(RegistrationFormEvent.NameValueChanged(it))
                    },
                    isError = uiState.nameErrorType != ErrorType.NONE,
                    isValid = uiState.isNameValid,
                    placeholder = stringResource(R.string.name_placeholder),
                    supportingText = when (uiState.nameErrorType) {
                        ErrorType.EMPTY -> {
                            { Text(text = stringResource(R.string.error_name_empty)) }
                        }

                        ErrorType.LENGTH -> {
                            { Text(text = stringResource(id = R.string.error_name_length)) }
                        }

                        else -> null
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(12.dp))
                TaskyTextField(
                    value = uiState.email,
                    onValueChange = {
                        onEvent(RegistrationFormEvent.EmailValueChanged(it))
                    },
                    isError = uiState.emailErrorType != ErrorType.NONE,
                    isValid = uiState.isEmailValid,
                    placeholder = stringResource(R.string.email_placeholder),
                    supportingText = when (uiState.emailErrorType) {
                        ErrorType.EMPTY -> {
                            { Text(text = stringResource(R.string.error_email_empty)) }
                        }

                        ErrorType.FORMAT -> {
                            { Text(text = stringResource(id = R.string.error_email_format)) }
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
                        onEvent(RegistrationFormEvent.PasswordValueChanged(it))
                    },
                    isError = uiState.passwordErrorType != ErrorType.NONE,
                    supportingText = when (uiState.passwordErrorType) {
                        ErrorType.EMPTY -> {
                            { Text(text = stringResource(R.string.error_password_empty)) }
                        }

                        ErrorType.LENGTH -> {
                            { Text(text = stringResource(id = R.string.error_password_length)) }
                        }

                        ErrorType.FORMAT -> {
                            { Text(text = stringResource(id = R.string.error_password_format)) }
                        }

                        else -> null
                    },
                    passwordVisible = uiState.isPasswordVisible,
                    onTrailingIconClick = {
                        onEvent(RegistrationFormEvent.TogglePasswordVisibility)
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(68.dp))
                TaskyButton(
                    text = stringResource(R.string.get_started_button),
                    onClick = {
                        keyboardController?.hide()
                        onEvent(RegistrationFormEvent.Submit)
                    },
                    enabled = uiState.isNameValid && uiState.isEmailValid && uiState.isPasswordValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                FloatingActionButton(
                    onClick = onBackClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(
                        TaskyIcons.NavigateBefore,
                        contentDescription = stringResource(id = R.string.navigate_up),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    TaskyTheme {
        RegisterScreen(
            onBackClick = {},
            uiState = RegistrationFormState(),
            onEvent = {},
            errors = emptyFlow(),
            successes = emptyFlow(),
        )
    }
}