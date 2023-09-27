package com.douglasbruce.tasky.features.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.douglasbruce.tasky.core.domain.validation.EmailValidator
import com.douglasbruce.tasky.core.domain.validation.NameValidator
import com.douglasbruce.tasky.core.domain.validation.PasswordValidator
import com.douglasbruce.tasky.features.register.form.RegistrationFormEvent
import com.douglasbruce.tasky.features.register.form.RegistrationFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

const val registrationStateKey = "registrationState"

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val nameValidator: NameValidator,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator
) : ViewModel() {

    var state by mutableStateOf(
        savedStateHandle[registrationStateKey] ?: RegistrationFormState()
    )
        private set

    fun onEvent(event: RegistrationFormEvent) {
        when (event) {
            is RegistrationFormEvent.NameValueChanged -> {
                val result = nameValidator(event.name)
                updateState(
                    state.copy(
                        name = event.name,
                        nameErrorType = result.errorType,
                        isNameValid = result.successful,
                    )
                )
            }

            is RegistrationFormEvent.EmailValueChanged -> {
                val result = emailValidator(event.email)
                updateState(
                    state.copy(
                        email = event.email,
                        emailErrorType = result.errorType,
                        isEmailValid = result.successful
                    )
                )
            }

            is RegistrationFormEvent.PasswordValueChanged -> {
                val result = passwordValidator(event.password)
                updateState(
                    state.copy(
                        password = event.password,
                        passwordErrorType = result.errorType,
                        isPasswordValid = result.successful
                    )
                )
            }

            is RegistrationFormEvent.PasswordVisibilityChanged -> {
                updateState(state.copy(isPasswordVisible = event.isPasswordVisible))
            }
        }
    }

    private fun updateState(newState: RegistrationFormState) {
        state = newState
        savedStateHandle[registrationStateKey] = newState
    }
}