package com.douglasbruce.tasky.features.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.common.utils.UiText
import com.douglasbruce.tasky.core.domain.repository.AuthRepository
import com.douglasbruce.tasky.core.domain.validation.EmailValidator
import com.douglasbruce.tasky.core.domain.validation.NameValidator
import com.douglasbruce.tasky.core.domain.validation.PasswordValidator
import com.douglasbruce.tasky.features.register.form.RegistrationFormEvent
import com.douglasbruce.tasky.features.register.form.RegistrationFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class RegisterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val nameValidator: NameValidator,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator
) : ViewModel() {

    var state by savedStateHandle.saveable {
        mutableStateOf(RegistrationFormState())
    }
        private set

    private val successChannel = Channel<UiText>()
    val successes = successChannel.receiveAsFlow()

    private val errorChannel = Channel<UiText>()
    val errors = errorChannel.receiveAsFlow()

    fun onEvent(event: RegistrationFormEvent) {
        when (event) {
            is RegistrationFormEvent.NameValueChanged -> {
                setAndValidateName(event.name)
            }

            is RegistrationFormEvent.EmailValueChanged -> {
                setAndValidateEmail(event.email)
            }

            is RegistrationFormEvent.PasswordValueChanged -> {
                setAndValidatePassword(event.password)
            }

            is RegistrationFormEvent.TogglePasswordVisibility -> {
                state = state.copy(isPasswordVisible = !state.isPasswordVisible)
            }

            is RegistrationFormEvent.Submit -> {
                if (state.isNameValid && state.isEmailValid && state.isPasswordValid) {
                    register(state.name, state.email, state.password)
                }
            }
        }
    }

    private fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.register(name, email, password)
            if (result is AuthResult.Success) {
                successChannel.send(
                    UiText.StringResource(
                        resId = R.string.success_account_creation,
                    )
                )
            } else if (result is AuthResult.Error) {
                result.message?.let {
                    errorChannel.send(
                        result.message
                    )
                }
            }
        }
    }

    private fun setAndValidateName(name: String) {
        val result = nameValidator.validate(name)
        state = state.copy(
            name = name,
            nameErrorType = result.errorType,
            isNameValid = result.successful,
        )
    }

    private fun setAndValidateEmail(email: String) {
        val result = emailValidator.validate(email)
        state = state.copy(
            email = email,
            emailErrorType = result.errorType,
            isEmailValid = result.successful
        )
    }

    private fun setAndValidatePassword(password: String) {
        val result = passwordValidator.validate(password)
        state = state.copy(
            password = password,
            passwordErrorType = result.errorType,
            isPasswordValid = result.successful
        )
    }
}